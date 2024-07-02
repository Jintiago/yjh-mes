// 回显数据
export function selectDictLabel(datas, value) {
  if (value === undefined) {
    return "";
  }
  var actions = [];

  Object.keys(datas).some((key) => {

    if (datas[key].id == ('' + value)) {
      actions.push(datas[key].name);
      return true;
    }
  })
  if (actions.length === 0) {
    actions.push(value);
  }
  return actions.join('');
}

// 格式化自定义字段(获取字段值)
export function fieldFormat(row,item){
  if(item.isSystem==='Y'){
    //系统自带属性
    //没有子属性
    let arr = item.field.split(".");
    if (arr.length<=1){
      return row[item.field];
    }else{
      let aon =arr[0];
      let son = arr[1];
      if(row[aon]==null){
        return '';
      }
      return row[aon][son];
    }
  }else{
    return row.extendFieldJson==null?'':row.extendFieldJson[item.field]
  }
}

// 格式化字典类型的自定义字段
export function dictFormat(item){
  return item.type.replace("dict:","");
}

// 格式化表单里面自定义字段对应的prop属性(获取字段名)
export function formPropFormat(item){
  if(item.isSystem==='Y'){
    //系统自带属性
    return item.field;
  }else{
    return 'extendFieldJson[item.field]'
  }
}
