import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	@org.junit.Test
	public void TEstlog(){
		String name = "hzh";
        Logger logger = LoggerFactory.getLogger(Test.class);
        logger.debug("Hello World");
        logger.info("hello {}", name);
        logger.warn("asd");
        logger.debug("asdsssss");
       System.out.println(name.indexOf("zhz")!=-1);
	}
}
