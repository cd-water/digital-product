<template>
  <div class="footer-container">
    <div class="top-part">
      <div class="top-item">
        <img src="@/assets/image/footer1.png" alt="" class="top-img" />
        <span class="top-text">品类齐全，品质保障</span>
      </div>
      <div class="top-item">
        <img src="@/assets/image/footer2.png" alt="" class="top-img" />
        <span class="top-text">商家优选，服务周到</span>
      </div>
      <div class="top-item">
        <img src="@/assets/image/footer3.png" alt="" class="top-img" />
        <span class="top-text">快递高效，安全无忧</span>
      </div>
      <div class="top-item">
        <img src="@/assets/image/footer4.png" alt="" class="top-img" />
        <span class="top-text">全天在线，随时下单</span>
      </div>
    </div>
    <div class="bottom-part">
      <div>
        <div class="bottom-text">关于我们</div>
        <ul>
          <li><RouterLink to="" class="link jump-link">平台介绍</RouterLink></li>
          <li><RouterLink to="" class="link jump-link">联系电话</RouterLink></li>
          <li><RouterLink to="" class="link jump-link">合作邮箱</RouterLink></li>
          <li><RouterLink to="" class="link jump-link">公司地址</RouterLink></li>
          <li><RouterLink to="" class="link jump-link">微信公众号</RouterLink></li>
        </ul>
      </div>
      <div>
        <div class="bottom-text">支付方式</div>
        <ul>
          <li><RouterLink to="" class="link jump-link">微信支付</RouterLink></li>
          <li><RouterLink to="" class="link jump-link">支付宝支付</RouterLink></li>
          <li><RouterLink to="" class="link jump-link">银联支付</RouterLink></li>
          <li><RouterLink to="" class="link jump-link">信用卡支付</RouterLink></li>
          <li><RouterLink to="" class="link jump-link">Apple Pay</RouterLink></li>
        </ul>
      </div>
      <div>
        <div class="bottom-text">售后保障</div>
        <ul>
          <li><RouterLink to="" class="link jump-link">退换货政策</RouterLink></li>
          <li><RouterLink to="" class="link jump-link">售后服务流程</RouterLink></li>
          <li><RouterLink to="" class="link jump-link">质保说明</RouterLink></li>
          <li><RouterLink to="" class="link jump-link">常见问题解答</RouterLink></li>
          <li><RouterLink to="" class="link jump-link">联系客服</RouterLink></li>
        </ul>
      </div>
      <div>
        <div class="bottom-text">提供服务</div>
        <ul>
          <li><RouterLink to="/customer/visitor-toall/product" class="link jump-link">数码产品购买</RouterLink></li>
          <li><RouterLink to="/customer/visitor-toall/accessory" class="link jump-link">数码配件购买</RouterLink></li>
          <li><RouterLink to="/customer/visitor-toall/digitalShop" class="link jump-link">数码店铺查看</RouterLink></li>
          <li><RouterLink to="/customer/visitor-toall/forum" class="link jump-link">数码论坛社区</RouterLink></li>
          <li><RouterLink to="" class="link jump-link">APP下载</RouterLink></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped>
.footer-container {
  background-color: #fff;
  border-radius: var(--border-radius-md);
}
.top-part {
  display: flex;
  align-items: center;
  justify-content: space-around;
  margin-top: var(--spacing-md);
}
.top-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}
.top-img {
  width: 80px;
  height: 80px;
}
.top-text {
  font-size: var(--font-size-lg);
}
.bottom-part {
  display: flex;
  align-items: center;
  justify-content: space-around;
  margin-top: var(--spacing-md);
}
.bottom-text {
  font-size: var(--font-size-lg);
}
.jump-link {
  transition: color 0.2s;
}
.jump-link:hover {
  color: orange;
}
</style>
