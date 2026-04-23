<template>
  <div class="main-content">
    <div v-if="showFlag">
      <!-- 搜索栏 -->
      <el-card shadow="never" class="search-card">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="竞赛名称">
            <el-input v-model="searchForm.jingsaimingcheng" placeholder="竞赛名称" clearable prefix-icon="el-icon-search" style="width:180px"></el-input>
          </el-form-item>
          <el-form-item label="竞赛类型">
            <el-input v-model="searchForm.jingsaileixing" placeholder="竞赛类型" clearable style="width:140px"></el-input>
          </el-form-item>
          <el-form-item label="学生姓名">
            <el-input v-model="searchForm.xueshengxingming" placeholder="学生姓名" clearable prefix-icon="el-icon-user" style="width:140px"></el-input>
          </el-form-item>
          <el-form-item label="审核状态">
            <el-select v-model="searchForm.sfsh" placeholder="全部" clearable style="width:110px">
              <el-option label="待审核" value="待审核"></el-option>
              <el-option label="已通过" value="已通过"></el-option>
              <el-option label="已驳回" value="已驳回"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="search()">查询</el-button>
            <el-button icon="el-icon-refresh" @click="resetSearch()">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="action-row">
          <el-button v-if="isAuth('jingsaibaoming','新增')" type="success" icon="el-icon-plus" @click="addOrUpdateHandler()">新增</el-button>
          <el-button
            v-if="isAuth('jingsaibaoming','删除')"
            :disabled="dataListSelections.length <= 0"
            type="danger"
            icon="el-icon-delete"
            @click="deleteHandler()"
          >批量删除</el-button>
        </div>
      </el-card>

      <!-- 数据表格 -->
      <el-card shadow="never" style="margin-top:16px">
        <el-table
          :data="dataList"
          border
          stripe
          v-loading="dataListLoading"
          element-loading-text="数据加载中..."
          style="width:100%"
          :header-cell-style="{background:'#f5f7fa',color:'#606266',fontWeight:'600'}"
          @selection-change="selectionChangeHandler">
          <el-table-column type="selection" width="50" align="center"></el-table-column>
          <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
          <el-table-column prop="jingsaimingcheng" label="竞赛名称" min-width="150" show-overflow-tooltip></el-table-column>
          <el-table-column prop="jingsaileixing" label="竞赛类型" width="100" align="center"></el-table-column>
          <el-table-column prop="cansaileixing" label="参赛类型" width="100" align="center"></el-table-column>
          <el-table-column prop="cansairenyuan" label="参赛人员" min-width="120" show-overflow-tooltip></el-table-column>
          <el-table-column prop="cansaizuopin" label="参赛作品" width="90" align="center">
            <template slot-scope="scope">
              <el-button v-if="scope.row.cansaizuopin" type="text" size="small" icon="el-icon-download" @click="download(scope.row.cansaizuopin)">下载</el-button>
              <span v-else style="color:#ccc">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="xuehao" label="学号" width="110" align="center"></el-table-column>
          <el-table-column prop="xueshengxingming" label="学生姓名" width="90" align="center"></el-table-column>
          <el-table-column prop="shenqingriqi" label="申请日期" width="110" align="center"></el-table-column>
          <el-table-column prop="ispay" label="是否支付" width="100" align="center">
            <template slot-scope="scope">
              <el-tag size="mini" :type="scope.row.ispay === '已支付' ? 'success' : 'info'">
                {{ scope.row.ispay === '已支付' ? '已支付' : '未支付' }}
              </el-tag>
              <el-button v-if="scope.row.ispay !== '已支付' && isAuth('jingsaibaoming','支付')" type="text" size="small" @click="payHandler(scope.row)">支付</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="shhf" label="审核回复" min-width="120" show-overflow-tooltip></el-table-column>
          <el-table-column prop="sfsh" label="审核状态" width="90" align="center">
            <template slot-scope="scope">
              <el-tag size="mini" :type="{'已通过':'success','待审核':'warning','已驳回':'danger'}[scope.row.sfsh] || 'info'">
                {{ scope.row.sfsh || '待审核' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column v-if="isAuth('jingsaibaoming','审核')" label="审核" width="70" align="center">
            <template slot-scope="scope">
              <el-button type="text" icon="el-icon-edit" size="small" @click="shDialog(scope.row)">审核</el-button>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="240" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button v-if="isAuth('jingsaibaoming','查看')" type="info" size="mini" icon="el-icon-tickets" @click="addOrUpdateHandler(scope.row.id,'info')">详情</el-button>
              <el-button v-if="isAuth('jingsaibaoming','评分')" type="warning" size="mini" icon="el-icon-star-off" @click="zuopindafenCrossAddOrUpdateHandler(scope.row,'cross')">评分</el-button>
              <el-button v-if="isAuth('jingsaibaoming','修改')" type="primary" size="mini" icon="el-icon-edit" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
              <el-button v-if="isAuth('jingsaibaoming','删除')" type="danger" size="mini" icon="el-icon-delete" @click="deleteHandler(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="sizeChangeHandle"
          @current-change="currentChangeHandle"
          :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper"
          style="margin-top:16px;text-align:right">
        </el-pagination>
      </el-card>
    </div>

    <!-- 添加/修改 -->
    <add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>

    <!-- 作品打分跨组件 -->
    <zuopindafen-cross-add-or-update v-if="zuopindafenCrossAddOrUpdateFlag" :parent="this" ref="zuopindafenCrossaddOrUpdate"></zuopindafen-cross-add-or-update>

    <!-- 审核弹窗 -->
    <el-dialog title="审核报名" :visible.sync="sfshVisiable" width="560px" :close-on-click-modal="false">
      <!-- 学生信息展示 -->
      <div v-if="shForm.id" class="review-info">
        <el-descriptions :column="2" border size="small" style="margin-bottom:16px">
          <el-descriptions-item label="竞赛名称" :span="2">
            <strong>{{ shForm.jingsaimingcheng }}</strong>
          </el-descriptions-item>
          <el-descriptions-item label="学生姓名">{{ shForm.xueshengxingming }}</el-descriptions-item>
          <el-descriptions-item label="学号">{{ shForm.xuehao }}</el-descriptions-item>
          <el-descriptions-item label="参赛类型">{{ shForm.cansaileixing || '-' }}</el-descriptions-item>
          <el-descriptions-item label="申请日期">{{ shForm.shenqingriqi || '-' }}</el-descriptions-item>
          <el-descriptions-item label="参赛人员" :span="2">{{ shForm.cansairenyuan || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <el-form :model="shForm" :rules="shRules" ref="shFormRef" label-width="90px">
        <el-form-item label="审核状态" prop="sfsh">
          <el-radio-group v-model="shForm.sfsh" @change="onShStatusChange">
            <el-radio-button label="已通过">
              <i class="el-icon-check"></i> 通过
            </el-radio-button>
            <el-radio-button label="已驳回">
              <i class="el-icon-close"></i> 驳回
            </el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="回复内容" prop="shhf">
          <el-input type="textarea" :rows="4" v-model="shForm.shhf"
                    :placeholder="shForm.sfsh === '已驳回' ? '请输入驳回原因（必填）' : '审核通过回复（选填）'"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="sfshVisiable = false">取 消</el-button>
        <el-button type="primary" @click="shHandler">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import AddOrUpdate from "./add-or-update";
import zuopindafenCrossAddOrUpdate from "../zuopindafen/add-or-update";

export default {
  data() {
    return {
      searchForm: {},
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: true,
      sfshVisiable: false,
      shForm: {},
      shRules: {
        sfsh: [{ required: true, message: '请选择审核状态', trigger: 'change' }],
        shhf: [{ validator: (rule, value, callback) => {
          if (this.shForm.sfsh === '已驳回' && (!value || !value.trim())) {
            callback(new Error('驳回时必须填写原因'))
          } else { callback() }
        }, trigger: 'blur' }]
      },
      addOrUpdateFlag: false,
      zuopindafenCrossAddOrUpdateFlag: false,
    };
  },
  components: { AddOrUpdate, zuopindafenCrossAddOrUpdate },
  created() {
    this.getDataList();
  },
  methods: {
    search() {
      this.pageIndex = 1;
      this.getDataList();
    },
    resetSearch() {
      this.searchForm = {};
      this.search();
    },
    getDataList() {
      this.dataListLoading = true;
      let params = { page: this.pageIndex, limit: this.pageSize, sort: 'id' };
      if (this.searchForm.jingsaimingcheng) params['jingsaimingcheng'] = '%' + this.searchForm.jingsaimingcheng + '%';
      if (this.searchForm.jingsaileixing) params['jingsaileixing'] = '%' + this.searchForm.jingsaileixing + '%';
      if (this.searchForm.xueshengxingming) params['xueshengxingming'] = '%' + this.searchForm.xueshengxingming + '%';
      if (this.searchForm.sfsh) params['sfsh'] = this.searchForm.sfsh;
      this.$http({ url: 'jingsaibaoming/page', method: 'get', params }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || [];
          this.totalPage = data.data.total || 0;
        } else {
          this.dataList = [];
          this.totalPage = 0;
        }
        this.dataListLoading = false;
      }).catch(() => {
        this.dataList = [];
        this.totalPage = 0;
        this.dataListLoading = false;
      });
    },
    sizeChangeHandle(val) { this.pageSize = val; this.pageIndex = 1; this.getDataList(); },
    currentChangeHandle(val) { this.pageIndex = val; this.getDataList(); },
    selectionChangeHandler(val) { this.dataListSelections = val; },
    addOrUpdateHandler(id, type) {
      this.showFlag = false;
      this.addOrUpdateFlag = true;
      this.zuopindafenCrossAddOrUpdateFlag = false;
      if (type !== 'info') type = 'else';
      this.$nextTick(() => { this.$refs.addOrUpdate.init(id, type); });
    },
    zuopindafenCrossAddOrUpdateHandler(row, type) {
      this.showFlag = false;
      this.addOrUpdateFlag = false;
      this.zuopindafenCrossAddOrUpdateFlag = true;
      this.$storage.set('crossObj', row);
      this.$storage.set('crossTable', 'jingsaibaoming');
      this.$nextTick(() => { this.$refs.zuopindafenCrossaddOrUpdate.init(row.id, type); });
    },
    payHandler(row) {
      this.$storage.set('paytable', 'jingsaibaoming');
      this.$storage.set('payObject', row);
      // 获取竞赛信息（收款码、收费标准等）
      if (row.jingsaixinxiid) {
        this.$http({ url: `jingsaixinxi/info/${row.jingsaixinxiid}`, method: 'get' }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$storage.set('payCompInfo', data.data);
          }
          this.$router.push('pay');
        }).catch(() => {
          this.$router.push('pay');
        });
      } else {
        this.$router.push('pay');
      }
    },
    shDialog(row) {
      if (row) {
        this.shForm = {
          id: row.id,
          gonghao: row.gonghao,
          jiaoshixingming: row.jiaoshixingming,
          jingsaimingcheng: row.jingsaimingcheng,
          jingsaileixing: row.jingsaileixing,
          cansaileixing: row.cansaileixing,
          cansairenyuan: row.cansairenyuan,
          cansaizuopin: row.cansaizuopin,
          cansaixuanyan: row.cansaixuanyan,
          shenqingriqi: row.shenqingriqi,
          xuehao: row.xuehao,
          xueshengxingming: row.xueshengxingming,
          sfsh: row.sfsh,
          shhf: row.shhf,
          ispay: row.ispay,
        };
        this.sfshVisiable = true;
      }
    },
    onShStatusChange() {
      if (this.$refs.shFormRef) this.$refs.shFormRef.clearValidate('shhf')
    },
    shHandler() {
      this.$refs.shFormRef.validate(valid => {
        if (!valid) return
        const isPass = this.shForm.sfsh === '已通过'
        this.$confirm(
          isPass
            ? `确认通过【${this.shForm.xueshengxingming}】的报名申请？`
            : `确认驳回【${this.shForm.xueshengxingming}】的报名申请？\n驳回原因：${this.shForm.shhf}`,
          '审核确认',
          { confirmButtonText: '确定', cancelButtonText: '取消', type: isPass ? 'success' : 'warning' }
        ).then(() => {
          this.$http({ url: 'jingsaibaoming/update', method: 'post', data: this.shForm }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$notify({
                title: isPass ? '审核通过' : '已驳回',
                message: isPass
                  ? `${this.shForm.xueshengxingming} 的报名已通过审核`
                  : `${this.shForm.xueshengxingming} 的报名已驳回`,
                type: isPass ? 'success' : 'warning',
                duration: 3000
              })
              this.getDataList()
              this.sfshVisiable = false
            } else {
              this.$message.error(data.msg)
            }
          })
        }).catch(() => {})
      })
    },
    download(file) {
      window.open(`${file}`);
    },
    deleteHandler(id) {
      var ids = id
        ? [Number(id)]
        : this.dataListSelections.map(item => Number(item.id));
      this.$confirm(`确定进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(() => {
        this.$http({ url: 'jingsaibaoming/delete', method: 'post', data: ids }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({ message: '操作成功', type: 'success', duration: 1500, onClose: () => { this.search(); } });
          } else {
            this.$message.error(data.msg);
          }
        });
      });
    },
  }
};
</script>

<style scoped>
.search-card { margin-bottom: 0; }
.action-row {
  padding-top: 10px;
  border-top: 1px dashed #ebeef5;
  margin-top: 10px;
}
</style>
