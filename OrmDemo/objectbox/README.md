## ObjectBox 
greenrobot 出了一个新的 NoSQL 数据库，greenrobot 称它是目前性能最好且易用的 NoSQL 数据库，且优于其它数据库 5~15 倍的性能。它不再是sqlite数据库，可以去它的[官网](http://objectbox.io/)。ObjectBox是该公司针对性能提升新出的数据库。  
![](img/img1.png)

### 优点
- **快** : 比测试过的其它数据库快 5~15 倍
- **面向对象的 API** : 没有 rows、columns 和 SQL，完全面向对象的 API
- **即时的单元测试** : 因为它是跨平台的，所以可以在桌面运行单元测试
- **简单的线程** : 它返回的对象可以在任何线程运转
- **不需要手动迁移** : 升级是完全自动的，不需要关心属性的变化以及命名的变化
- 可以

### 不足
- 有点类似greenDAO的缺点。

### 使用
`data-observers-reactive-extensions`：可以实时监测到数据库的数据变化；  

    buildscript {
		repositories {
        	jcenter()
        	mavenCentral()
        	maven { url "http://objectbox.net/beta-repo/" }
    	}
    	dependencies {
        	classpath 'com.android.tools.build:gradle:2.3.3'
        	classpath 'io.objectbox:objectbox-gradle-plugin:0.9.12.1'//for objectBox

    	}
	}

**build.gradle**  
`apply plugin: 'io.objectbox'`  

	dependencies {
    	compile fileTree(dir: 'libs', include: ['*.jar'])
    	compile 'io.objectbox:objectbox-android:0.9.12'
	}
  
#### 数据库迁移  
&emsp;**1、重命名实体或属性**  
&emsp;&emsp; 1、声明空注解`@Uid`

    @Entity
    @Uid
    public class MyName {
    	@Uid
    	String myProperty;
     
    	// ...
    }  
  
&emsp;&emsp;2、编译你的工程，UID注解值会重写  
  
    @Entity
    @Uid(234798435324L)
    	public class MyName {
    	@Uid(8973457593L)
    	String myProperty;
     
    	// ...
    }
    
&emsp;&emsp;3、重命名  
  
    @Entity
    @Uid(234798435324L)
    public class MyNewName {
    	@Uid(8973457593L)
    	String renamedProperty;
     
    	// ...
    }  
  
&emsp;&emsp; **也可以手动更新**  
&emsp;&emsp; -在objectbox-models/default.json找到需要更新的实体或属性  
&emsp;&emsp; -直接使用`@Uid(...)`注解	
  
&emsp;**2、修改属性数据类型**  
&emsp;&emsp; 1、声明注解`@Uid(-1)`  
  
    @Uid(-1)
    String year;  
  
&emsp;&emsp;2、编译你的工程，UID注解值会重写  
  
    @Uid(8973457594L)
    int year;  
  
&emsp;&emsp; **也可以手动更新**  
&emsp;&emsp; 步骤同上

#### 关联  
&emsp;**1、1:1关联**  
&emsp;&emsp; **ToOne<Target>**

    @Entity
    public class Order {
    @Id long id;
     
    ToOne<Customer> customer; // Customer is another @Entity class
    }
  
&emsp;&emsp; **@Relation**  
  
    @Entity
    public class Order {
    @Id long id;

	long myCustomerId;
     
    @Relation(idProperty = "myCustomerId")
    Customer customer; // Customer is another @Entity class
    }  
  
&emsp;**2、1:N关联**  
  
    @Entity
    public class Customer {
    @Id long id;
     
    @Backlink
    List<Order> orders;
    }
     
    @Entity
    public class Order {
    @Id long id;
     
    @Relation Customer customer;
    }
