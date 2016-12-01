## KScheme
一个scheme的解析器，努力把它实现。# KScheme

## 改变

### * 可变参数  
```scheme

(define a (lambda (x z y ... . w) (list y w)))
(a 1 2 3 4 5 (list 99 88))

output:
KScheme>((3 4 5) (99 88))

```

"...",列表参数，kscheme将"..."左邻居绑定为列表参数，并且列表参数必须在普通参数的后面
"."，点对参数，kscheme讲"."右邻居绑定为点对参数，点对参数的必须为一个列表，并且点对参数必须在参数的最后

