package guru.springframework.controllers;

import java.util.Date;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.model.events.PageViewEvent;
import guru.springframework.pageview.PageViewService;
import guru.springframework.services.ProductService;

/**
 * Created by jt on 1/20/16.
 */
@Controller
public class IndexController {

	private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    private ProductService productService;
    private PageViewService pageViewService;

    @Autowired
    public IndexController(ProductService productService, PageViewService pageViewService) {
        this.productService = productService;
        this.pageViewService = pageViewService;
    }

    @RequestMapping({"/", "index"})
    public String getIndex(Model model) throws JAXBException{

        model.addAttribute("products", productService.listProducts());

        //Send Page view event
        PageViewEvent pageViewEvent = new PageViewEvent();
        pageViewEvent.setPageUrl("springframework.guru/");
        pageViewEvent.setPageViewDate(new Date());
        pageViewEvent.setCorrelationId(UUID.randomUUID().toString());

        log.info("Sending Message to page view service");
        pageViewService.sendPageViewEvent(pageViewEvent);

        return "index";
    }

}
