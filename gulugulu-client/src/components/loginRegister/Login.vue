<template>
  <el-input
    v-model="user.userName"
    style="width: 455px"
    placeholder="请输入姓名 ~咕噜~"
  >
    <template #prepend>
      <span class="form_info">账号</span>
    </template>
  </el-input>
  <el-input
    v-model="user.password"
    style="width: 455px"
    type="password"
    placeholder="请输入密码 ~咕噜~"
    show-password
    @focus="handlechangeImg(1)"
    @blur="handlechangeImg(0)"
  >
    <template #prepend>
      <span class="form_info">密码</span>
    </template></el-input
  >
  <div class="btn-wp">
    <button class="btn-register btn" @click="changeVisible(1)">
      <span>注册</span>
    </button>
    <button class="btn-login btn" @click="handleBeforeLogin">
      <span>登录</span>
    </button>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import { handleLogin } from "@/hooks/userAuth";

const emit = defineEmits(["changeVisible", "changeImg"]);
//用户
let user = reactive({
  userName: "",
  password: "",
});

//展示注册界面
const changeVisible = (number) => {
  emit("changeVisible", number);
};

//修改两侧图片
const handlechangeImg = (imgData) => {
  emit("changeImg", imgData);
};

//用户登录
function handleBeforeLogin() {
  handleLogin(user);
}
</script>

<style scoped>
.form_info {
  color: var(--BK1);
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
  justify-content: space-between;
}

.btn {
  border: 1px solid #e4e8e8;
  border-radius: 8px;
  background-color: #fff;
  width: 190px;
  height: 40px;
}
.btn-login {
  background-color: var(--Bl2);
}
.btn-login:hover {
  background-color: var(--Bl3);
}

.btn-login span {
  color: var(--color-text-main);
}
</style>
