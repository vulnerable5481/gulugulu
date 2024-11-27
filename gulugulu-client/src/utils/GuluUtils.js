// 判断数据是否为空
export function isEmpty(obj) {
  if (obj == null) {
    //处理null和 undefined
    return true;
  }
  if (typeof obj === 'string') {
    //处理字符串
    return obj.trim().length === 0;
  }
  if (typeof obj === 'object') {
    //处理对象
    return Object.keys(obj).length === 0;
  }
  if (Array.isArray(value)) {
    // 处理数组
    return value.length === 0;
  }
}

// 处理日期，格式化 年月日
export function convertTime(time) {
  time = Math.round(time);
  let h = 0;
  let m = 0;
  let s = 0;

  h = Math.floor(time / 3600);
  time %= 3600;
  m = Math.floor(time / 60);
  time %= 60;
  s = time;

  // 格式化秒数和分钟
  let mm = m < 10 ? '0' + m : m;
  let ss = s < 10 ? '0' + s : s;

  // 如果小时为0，返回 'mm:ss' 格式，否则返回 'hh:mm:ss' 格式
  return h > 0 ? `${h}:${mm}:${ss}` : `${mm}:${ss}`;
}
