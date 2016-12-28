- 参数绑定
  `@RequestHeader` 大小写不敏感; `@PathVariable、@RequestParam、@CookieValue` 都是大小写敏感
- 参数类型
  Spring 封装的Errors 和BindingResult 对象。 这两个对象参数必须紧接在需要验证的实体对象参数之后，它里面包含了实体对象的验证结果。`@RequestMapping` 可以使用 `@Validated`与`BindingResult`联合验证输入的参数，在验证通过和失败的情况下，分别返回不同的视图。
- 数据共享
  数据的保存和传递方式两种：
    - `@ModelAttribute`
        标注在方法上：被标注方法在`@RequestMapping`方法执行前执行，返回对象放在模型(Model)属性中
        标注在`@RequestMapping`方法的参数上：该参数值从模型或者Session中取对应名称的属性值

    - `@SessionAttributes`
        第一次访问处理器方法完成后Spring才会把模型中对应属性放到Session中，从第二次访问开始才会真正从Session中取值