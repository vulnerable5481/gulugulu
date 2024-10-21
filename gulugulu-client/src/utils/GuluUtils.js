//判断数据是否为空
export function isEmpty(obj) {
  if (obj == null) {
    //处理null和 undefined
    return true;
  }
  if (typeof obj === "string") {
    //处理字符串
    return obj.trim().length === 0;
  }
  if (typeof obj === "object") {
    //处理对象
    return Object.keys(obj).length === 0;
  }
  if (Array.isArray(value)) {
    // 处理数组
    return value.length === 0;
  }
}
