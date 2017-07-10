## Ormlite 
首先可以去它的[官网](https://www.ormlite.com)，它的英文全称是Object Relational Mapping，意思是对象关系映射。  
![](img/orm.png)  

### 优点
- 轻量级
- 使用简单，易上手
- 封装完善
- 文档全面

### 不足
- 基于反射，效率较低
- 最新release版本5.0，维护日期2016/07/27

### 使用
只需要直接导入对应jar包依赖库即可  
#### 定义实体类Bean，代表一张表
  
	public SimpleData() {
        // needed by ormlite，otherwise↓
        // Caused by: java.lang.IllegalArgumentException: Can't find a no-arg constructor for class com.harbor.ormlite.SimpleData
    }
	public SimpleData(long millis){
		super();
		...
	}  
  
#### 关联外键  
  
	// 外部对象字段
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    public PackageInfo mPackage;  
  
	   /**
     	* 这里需要注意的是：属性类型只能是ForeignCollection<T>或者Collection<T>
     	* 如果需要懒加载（延迟加载）可以在@ForeignCollectionField加上参数eager=false
     	* 这个属性也就说明一个部门对应着多个用户
        */
	// 一个套餐可以对应多个主题
	@ForeignCollectionField(eager = true) // 必须
	public ForeignCollection<Theme> themes;

