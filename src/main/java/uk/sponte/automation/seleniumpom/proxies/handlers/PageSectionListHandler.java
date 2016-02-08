package uk.sponte.automation.seleniumpom.proxies.handlers;

import com.google.inject.Provider;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageElementImpl;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Invocation handler for page section lists
 * Created by n450777 on 08/04/15.
 */
public class PageSectionListHandler implements InvocationHandler {
    private DependencyInjector driver;
    private SearchContext searchContext;
    private By by;
    private Type pageSectionType;
    private Provider<PageFactory> pageFactory;
    private FrameWrapper frame;
    private WebDriverFrameSwitchingOrchestrator webDriverOrchestrator;

    public PageSectionListHandler(
            DependencyInjector driver,
            SearchContext searchContext,
            By by,
            Type pageSectionType,
            Provider<PageFactory> pageFactory,
            FrameWrapper frame,
            WebDriverFrameSwitchingOrchestrator webDriverOrchestrator) {
        this.driver = driver;
        this.searchContext = searchContext;
        this.by = by;
        this.pageSectionType = pageSectionType;
        this.pageFactory = pageFactory;
        this.frame = frame;
        this.webDriverOrchestrator = webDriverOrchestrator;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<WebElement> elements = searchContext.findElements(by);
        List<Object> pageSections = new ArrayList<Object>();
        for (WebElement element : elements) {
            PageElement pageElement = new PageElementImpl(driver, element);

//            InvocationHandler pageElementHandler = new PageElementHandler(pageElement, frame, webDriverOrchestrator);
//            PageElement instance = (PageElement)Proxy.newProxyInstance(
//                    PageElement.class.getClassLoader(),
//                    new Class[]{PageElement.class},
//                    pageElementHandler);

            Class<?> pageSectionClass = (Class<?>) this.pageSectionType;
            Object pageSection = pageFactory.get().get(pageSectionClass, pageElement, frame);
            pageSections.add(pageSection);
        }

        return method.invoke(pageSections, args);
    }
}
