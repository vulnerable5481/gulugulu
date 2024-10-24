<template>
  <el-input
    v-model="user.account"
    style="width: 455px"
    placeholder="请输入手机号"
  >
    <template #prepend>
      <span class="form_info">+86</span>
    </template>
    <template #append>
      <span class="form_info" style="color: var(--BK2);">获取验证码</span>
    </template>
  </el-input>
  <el-input
    v-model="user.pwd"
    style="width: 455px"
    type="password"
    placeholder="请输入验证码"
    show-password
  >
    <template #prepend>
      <span class="form_info">验证码</span>
    </template></el-input
  >
  <div class="btn-wp">
    <button class="btn">登录/注册</button>
  </div>
</template>

<script setup>
import { reactive } from "vue";

let user = reactive({
  account: "",
  pwd: "",
});
</script>

<style scoped>
.form_info {
  color: var(--BK2);
}

.el-icon ::v-depp svg {
  width: 31em;
  height: 3em;
}

::v-deep .el-input-group__prepend {
  background-color: #fff;
}

.el-input ::v-deep .el-input__wrapper {
  height: 50px;
  box-shadow: none;
  border-top: 1px solid #e4e8e8;
  border-right: 1px solid #e4e8e8;
  border-bottom: 1px solid #e4e8e8;
}

::v-deep .el-input-group__prepend {
  width: 65.16px;
  box-shadow: none;
  background-color: #fff;
  border-top: 1px solid var(--GR1);
  border-left: 1px solid var(--GR1);
  border-bottom: 1px solid var(--GR1);
}

.btn-wp {
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn {
  border: 1px solid #e4e8e8;
  border-radius: 8px;
  background-color: #fff;
  width: 190px;
  height: 40px;
  color: #fff;
  background-color: var(--Bl2);
}

.btn:hover {
  background-color: var(--Bl3);
}
</style>
