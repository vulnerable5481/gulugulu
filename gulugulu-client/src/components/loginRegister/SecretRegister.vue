<template>
  <!-- 名字 -->
  <el-input
    v-model="user.userName"
    style="width: 455px"
    placeholder="请输入姓名  ~咕噜~"
  >
    <template #prepend>
      <span class="form_info">账号</span>
    </template>
  </el-input>
  <!-- 密码 -->
  <el-input
    v-model="user.password"
    style="width: 455px"
    type="password"
    placeholder="请输入密码  ~咕噜~"
    show-password
    @focus="handlechangeImg(1)"
    @blur="handlechangeImg(0)"
  >
    <template #prepend>
      <span class="form_info">密码</span>
    </template></el-input
  >
  <!-- 确认密码 -->
  <el-input
    v-model="user.mksuerPwd"
    style="width: 455px"
    type="password"
    placeholder="~o( =∩ω∩= )m~"
    show-password
    @focus="handlechangeImg(1)"
    @blur="handlechangeImg(0)"
  >
    <template #prepend>
      <span class="form_info">确认密码</span>
    </template></el-input
  >
  <div class="btn-wp">
    <el-button
      class="btn-register btn"
      :plain="true"
      @click="handleRegister(user)"
      >注册</el-button
    >
  </div>
</template>

<script setup>
import { reactive } from "vue";
import { handleRegister } from "@/hooks/userHooks";

const emit = defineEmits(["changeImg"]);
//用户注册信息
let user = reactive({
  userName: "",
  password: "",
  mksuerPwd: "",
});

//修改两侧图片
const handlechangeImg = (imgData) => {
  emit("changeImg", imgData);
};
</script>

<style scoped>
.form_info {
  color: var(--BK2);
}

.el-icon ::v-depp svg {
  width: 31em;
  height: 3em;
}

.el-input ::v-deep .el-input__wrapper {
  height: 50px;
  box-shadow: none;
  border-top: 1px solid #e4e8e8;
  border-right: 1px solid #e4e8e8;
  border-bottom: 1px solid #e4e8e8;
}

::v-deep .el-input-group__prepend {
  background-color: #fff;
  width: 65.16px;
  box-shadow: none;
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
}
.btn-register {
  background-color: var(--Bl2);
}
.btn-register:hover {
  background-color: var(--Bl3);
}

.btn-register {
  color: var(--color-text-main);
}
</style>
