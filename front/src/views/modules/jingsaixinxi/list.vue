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
          <el-form-item label="竞赛地点">
            <el-input v-model="searchForm.jingsaididian" placeholder="竞赛地点" clearable style="width:140px"></el-input>
          </el-form-item>
          <el-form-item label="竞赛级别">
            <el-select v-model="searchForm.jingsaijib" placeholder="全部" clearable style="width:110px">
              <el-option label="校级" value="校级"></el-option>
              <el-option label="省级" value="省级"></el-option>
              <el-option label="国家级" value="国家级"></el-option>
              <el-option label="国际级" value="国际级"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="竞赛状态">
            <el-select v-model="searchForm.zhuangtai" placeholder="全部" clearable style="width:100px">
              <el-option label="报名中" value="报名中"></el-option>
              <el-option label="进行中" value="进行中"></el-option>
              <el-option label="已结束" value="已结束"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="search()">查询</el-button>
            <el-button icon="el-icon-refresh" @click="resetSearch()">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="action-row">
          <el-button v-if="isAuth('jingsaixinxi','新增')" type="success" icon="el-icon-plus" @click="addOrUpdateHandler()">新增</el-button>
          <el-button
            v-if="isAuth('jingsaixinxi','删除')"
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
          <el-table-column prop="jingsaimingcheng" label="竞赛名称" min-width="160" show-overflow-tooltip></el-table-column>
          <el-table-column prop="jingsaileixing" label="竞赛类型" width="100" align="center"></el-table-column>
          <el-table-column prop="jingsaijib" label="竞赛级别" width="90" align="center">
            <template slot-scope="scope">
              <el-tag size="mini" :type="{'国际级':'danger','国家级':'warning','省级':'primary','校级':'info'}[scope.row.jingsaijib] || 'info'" effect="plain">
                {{ scope.row.jingsaijib || '-' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="zhuangtai" label="状态" width="90" align="center">
            <template slot-scope="scope">
              <el-tag size="mini" :type="{'报名中':'success','进行中':'warning','已结束':'info'}[scope.row.zhuangtai] || ''">
                {{ scope.row.zhuangtai || '-' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="baomingjiezhi" label="报名截止" width="120" align="center"></el-table-column>
          <el-table-column prop="jingsaididian" label="竞赛地点" width="120" show-overflow-tooltip></el-table-column>
          <el-table-column prop="jingsaishijian" label="竞赛时间" width="120" align="center"></el-table-column>
          <el-table-column prop="moshi" label="模式" width="80" align="center"></el-table-column>
          <el-table-column prop="fengmiantupian" label="封面图片" width="100" align="center">
            <template slot-scope="scope">
              <img v-if="scope.row.fengmiantupian" :src="scope.row.fengmiantupian.split(',')[0]" width="60" height="60" style="border-radius:4px;object-fit:cover" />
              <span v-else style="color:#ccc">无图片</span>
            </template>
          </el-table-column>
          <el-table-column prop="jiaoshixingming" label="负责教师" width="90" align="center"></el-table-column>
          <el-table-column label="操作" width="260" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button v-if="isAuth('jingsaixinxi','查看')" type="info" size="mini" icon="el-icon-tickets" @click="addOrUpdateHandler(scope.row.id,'info')">详情</el-button>
              <el-button v-if="isAuth('jingsaixinxi','报名')" type="success" size="mini" icon="el-icon-edit-outline" @click="jingsaibaomingCrossAddOrUpdateHandler(scope.row,'cross')">报名</el-button>
              <el-button v-if="isAuth('jingsaixinxi','修改')" type="primary" size="mini" icon="el-icon-edit" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
              <el-button v-if="isAuth('jingsaixinxi','删除')" type="danger" size="mini" icon="el-icon-delete" @click="deleteHandler(scope.row.id)">删除</el-button>
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

    <!-- 竞赛报名跨组件 -->
    <jingsaibaoming-cross-add-or-update v-if="jingsaibaomingCrossAddOrUpdateFlag" :parent="this" ref="jingsaibaomingCrossaddOrUpdate"></jingsaibaoming-cross-add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from "./add-or-update";
import jingsaibaomingCrossAddOrUpdate from "../jingsaibaoming/add-or-update";

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
      addOrUpdateFlag: false,
      jingsaibaomingCrossAddOrUpdateFlag: false,
    };
  },
  components: { AddOrUpdate, jingsaibaomingCrossAddOrUpdate },
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
      if (this.searchForm.jingsaididian) params['jingsaididian'] = '%' + this.searchForm.jingsaididian + '%';
      if (this.searchForm.jingsaijib) params['jingsaijib'] = this.searchForm.jingsaijib;
      if (this.searchForm.zhuangtai) params['zhuangtai'] = this.searchForm.zhuangtai;
      this.$http({ url: 'jingsaixinxi/page', method: 'get', params }).then(({ data }) => {
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
      this.jingsaibaomingCrossAddOrUpdateFlag = false;
      if (type !== 'info') type = 'else';
      this.$nextTick(() => { this.$refs.addOrUpdate.init(id, type); });
    },
    jingsaibaomingCrossAddOrUpdateHandler(row, type) {
      // 校验竞赛状态
      if (row.zhuangtai && row.zhuangtai !== '报名中') {
        this.$message.error('该竞赛当前不在报名阶段');
        return;
      }
      // 校验报名截止时间
      if (row.baomingjiezhi) {
        const deadline = new Date(row.baomingjiezhi.replace(/-/g, '/'));
        if (new Date() > deadline) {
          this.$message.error('报名已截止，无法继续报名');
          return;
        }
      }
      this.showFlag = false;
      this.addOrUpdateFlag = false;
      this.jingsaibaomingCrossAddOrUpdateFlag = true;
      this.$storage.set('crossObj', row);
      this.$storage.set('crossTable', 'jingsaixinxi');
      this.$nextTick(() => { this.$refs.jingsaibaomingCrossaddOrUpdate.init(row.id, type); });
    },
    deleteHandler(id) {
      var ids = id
        ? [Number(id)]
        : this.dataListSelections.map(item => Number(item.id));
      this.$confirm(`确定进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(() => {
        this.$http({ url: 'jingsaixinxi/delete', method: 'post', data: ids }).then(({ data }) => {
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
