package org.shaolin.bmdp.performance;

public aspect HelloAspect {

	pointcut HelloWorldPointCut(int i) : execution(* org.shaolin.bmdp.performance.HelloWorld.main(int)) && args(i);

	before(int i) : HelloWorldPointCut(i){
		System.out.println("Before Hello world: " + i);
	}

	after(int i) : HelloWorldPointCut(i){
		i += 1;
		System.out.println("After Hello world: " + i);
	}

//	int around(int x) : HelloWorldPointCut(x){
//		System.out.println("Entering : " + thisJoinPoint.getSourceLocation());
//		int newValue = proceed(x * 3);
//		return newValue;
//	}
}
