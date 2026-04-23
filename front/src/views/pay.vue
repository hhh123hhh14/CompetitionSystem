<template>
  <div class="pay-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="header-icon"><i class="el-icon-wallet"></i></div>
      <div class="header-text">
        <div class="header-title">报名费支付</div>
        <div class="header-sub">请扫描下方收款码完成支付，支付完成后点击"我已支付"</div>
      </div>
    </div>

    <el-row :gutter="20">
      <!-- 左侧：订单信息 -->
      <el-col :span="14">
        <el-card shadow="never" class="pay-card">
          <div class="pay-section-title"><i class="el-icon-document"></i> 订单信息</div>
          <el-descriptions :column="2" border size="medium" class="order-info">
            <el-descriptions-item label="竞赛名称" :span="2">
              <strong>{{ orderInfo.jingsaimingcheng || '-' }}</strong>
            </el-descriptions-item>
            <el-descriptions-item label="竞赛类型">{{ orderInfo.jingsaileixing || '-' }}</el-descriptions-item>
            <el-descriptions-item label="竞赛级别">
              <el-tag v-if="compInfo.jingsaijib" size="mini" :type="jibType(compInfo.jingsaijib)">{{ compInfo.jingsaijib }}</el-tag>
              <span v-else>-</span>
            </el-descriptions-item>
            <el-descriptions-item label="学生姓名">{{ orderInfo.xueshengxingming || '-' }}</el-descriptions-item>
            <el-descriptions-item label="学号">{{ orderInfo.xuehao || '-' }}</el-descriptions-item>
            <el-descriptions-item label="参赛类型">{{ orderInfo.cansaileixing || '-' }}</el-descriptions-item>
            <el-descriptions-item label="申请日期">{{ orderInfo.shenqingriqi || '-' }}</el-descriptions-item>
            <el-descriptions-item label="收费标准" :span="2">
              <span class="fee-amount">{{ compInfo.shoufeibiaozhun || '详见竞赛说明' }}</span>
            </el-descriptions-item>
          </el-descriptions>

          <!-- 支付方式选择 -->
          <div class="pay-section-title" style="margin-top:24px"><i class="el-icon-bank-card"></i> 支付方式</div>
          <div class="pay-type-grid">
            <div class="pay-type-item"
                 :class="{ active: type === item.label }"
                 v-for="item in payMethods"
                 :key="item.label"
                 @click="type = item.label">
              <img :src="item.img" alt="" class="pay-icon" />
              <span class="pay-name">{{ item.label }}</span>
              <i v-if="type === item.label" class="el-icon-success pay-check"></i>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：收款码 -->
      <el-col :span="10">
        <el-card shadow="never" class="pay-card qrcode-card">
          <div class="pay-section-title"><i class="el-icon-picture-outline"></i> 扫码支付</div>
          <div class="qrcode-wrapper">
            <div v-if="qrcodeUrl" class="qrcode-box">
              <img :src="qrcodeUrl" class="qrcode-img" />
              <div class="qrcode-tip">请使用微信/支付宝扫描上方二维码</div>
            </div>
            <div v-else class="qrcode-empty">
              <i class="el-icon-warning-outline" style="font-size:40px;color:#e6a23c"></i>
              <p>暂无收款码</p>
              <p style="font-size:12px;color:#c0c4cc">请联系竞赛负责人获取支付方式</p>
            </div>
          </div>
          <div v-if="compInfo.shoufeibiaozhun" class="fee-display">
            <div class="fee-label">应付金额</div>
            <div class="fee-value">{{ compInfo.shoufeibiaozhun }}</div>
          </div>
          <div class="pay-actions">
            <el-button type="success" icon="el-icon-check" size="medium" :disabled="!type" @click="submitTap" style="width:100%">
              我已支付
            </el-button>
            <el-button icon="el-icon-back" size="medium" @click="back()" style="width:100%;margin-top:10px;margin-left:0">
              返回
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      type: "",
      table: "",
      orderInfo: {},
      compInfo: {},
      payMethods: [
        { label: '微信支付', img: require('@/assets/img/test/weixin.png') },
        { label: '支付宝支付', img: require('@/assets/img/test/zhifubao.png') },
        { label: '建设银行', img: require('@/assets/img/test/jianshe.png') },
        { label: '农业银行', img: require('@/assets/img/test/nongye.png') },
        { label: '中国银行', img: require('@/assets/img/test/zhongguo.png') },
        { label: '交通银行', img: require('@/assets/img/test/jiaotong.png') }
      ]
    };
  },
  computed: {
    qrcodeUrl() {
      // 优先使用竞赛配置的收款码，其次使用默认收款码
      if (this.compInfo.shoukuanma) return this.compInfo.shoukuanma
      // 无收款码
      return ''
    }
  },
  mounted() {
    this.table = this.$storage.get("paytable");
    this.orderInfo = this.$storage.getObj("payObject") || {};
    this.compInfo = this.$storage.getObj("payCompInfo") || {};
  },
  methods: {
    jibType(jib) {
      return { '国际级': 'danger', '国家级': 'warning', '省级': 'primary', '校级': 'info' }[jib] || 'info'
    },
    submitTap() {
      if (!this.type) {
        this.$message.error("请选择支付方式");
        return;
      }
      this.$confirm(
        `您选择了【${this.type}】进行支付\n\n请确认您已通过扫码完成付款，确认后状态将更新为"已支付"。\n\n注意：如未实际付款即点击确认，可能导致报名无效。`,
        '支付确认',
        {
          confirmButtonText: '确认已支付',
          cancelButtonText: '取消',
          type: 'warning',
          dangerouslyUseHTMLString: false
        }
      ).then(() => {
        this.orderInfo.ispay = "已支付";
        this.$http({
          url: `${this.table}/update`,
          method: "post",
          data: this.orderInfo
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$notify({
              title: '支付成功',
              message: `【${this.orderInfo.jingsaimingcheng}】报名费已支付完成`,
              type: 'success',
              duration: 3000
            });
            setTimeout(() => {
              this.$router.go(-1);
            }, 1000);
          } else {
            this.$message.error(data.msg);
          }
        });
      }).catch(() => {});
    },
    back() {
      this.$router.go(-1);
    }
  }
};
</script>
<style lang="scss" scoped>
.pay-page {
  padding: 0;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
  padding: 24px 28px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: #fff;

  .header-icon {
    width: 48px;
    height: 48px;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    backdrop-filter: blur(8px);
    flex-shrink: 0;
  }

  .header-title {
    font-size: 20px;
    font-weight: 700;
  }

  .header-sub {
    font-size: 13px;
    opacity: 0.8;
    margin-top: 2px;
  }
}

.pay-card {
  border-radius: 12px !important;
  border: 1px solid #e4e7ed !important;

  &::v-deep .el-card__body {
    padding: 24px;
  }
}

.pay-section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;

  i {
    margin-right: 6px;
    color: #667eea;
  }
}

.order-info {
  margin-bottom: 8px;
}

.fee-amount {
  color: #f56c6c;
  font-weight: 700;
  font-size: 16px;
}

.pay-type-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.pay-type-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  border: 2px solid #ebeef5;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;

  &:hover {
    border-color: #c0c4cc;
    background: #fafafa;
  }

  &.active {
    border-color: #667eea;
    background: #f0f3ff;
    box-shadow: 0 2px 12px rgba(102, 126, 234, 0.15);
  }

  .pay-icon {
    width: 32px;
    height: 32px;
    object-fit: contain;
    flex-shrink: 0;
  }

  .pay-name {
    font-size: 14px;
    color: #303133;
    font-weight: 500;
    flex: 1;
  }

  .pay-check {
    color: #667eea;
    font-size: 18px;
  }
}

.qrcode-card {
  text-align: center;
}

.qrcode-wrapper {
  padding: 20px 0;
}

.qrcode-box {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.qrcode-img {
  width: 240px;
  height: 240px;
  object-fit: contain;
  border-radius: 8px;
  border: 2px solid #ebeef5;
  padding: 8px;
  background: #fff;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.qrcode-tip {
  margin-top: 12px;
  font-size: 13px;
  color: #909399;
}

.qrcode-empty {
  padding: 40px 0;
  color: #909399;

  p {
    margin-top: 8px;
    font-size: 14px;
  }
}

.fee-display {
  background: linear-gradient(135deg, #fff9ef, #fff3e0);
  border: 1px solid #ffecd2;
  border-radius: 8px;
  padding: 12px 16px;
  margin-bottom: 16px;
  text-align: center;

  .fee-label {
    font-size: 13px;
    color: #909399;
    margin-bottom: 4px;
  }

  .fee-value {
    font-size: 24px;
    font-weight: 700;
    color: #f56c6c;
  }
}

.pay-actions {
  padding-top: 12px;
}
</style>
