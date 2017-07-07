## greenDAO 
首先可以去它的[官网](http://greenrobot.org/greendao/)，它的英文全称是Object Relational Mapping，意思是对象关系映射。
### 优点
- 效率很高，插入和更新的速度是sqlite的2倍，加载实体的速度是ormlite的4.5倍。官网测试结果：[http://greendao-orm.com/features/](http://greendao-orm.com/features/)
- 文件较小（<100K），占用更少的内存 ，但是需要create Dao，
- 操作实体灵活：支持get，update，delete等操作
- 使用过GreenDao的同学都知道，3.0之前需要通过新建GreenDaoGenerator工程生成Java数据对象（实体）和DAO对象，非常的繁琐而且也加大了使用成本。**GreenDao3.0**最大的变化就是采用注解的方式通过编译方式生成Java数据对象和DAO对象。

### 不足
- 学习成本较高。其中使用了一个Java工程根据一些属性和规则去generate一些基础代码，类似于javaBean但会有一些规则，另外还有QueryBuilder、Dao等API，所以首先要明白整个过程，才能方便使用。没有ORMLite那样封装的完整，不过greenDao的官网上也提到了这一点，正是基于generator而不是反射，才使得其效率高的多。

