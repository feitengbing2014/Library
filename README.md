# Library
个人总结的一些可用的框架，同时把一些至今开发过程中用的非常频繁的类和方法放入其中

工程三部分：
1.architect 总结了一些app内部出现的很多的错误，配合IBaseView使用，在app内部可以将各种事件转化成Error进行处理

2.lib 总结了一些常用的工具类，还有一些activity，fragment里面常用的方法

3.net 将app发起请求请到接口返回数据最终回调返回接口的过程抽象出来，整个抽象过程目前与okhttp结合使用，当然也可以与其他http框架结合使用
