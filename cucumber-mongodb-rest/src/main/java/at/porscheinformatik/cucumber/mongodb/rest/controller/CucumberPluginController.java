package at.porscheinformatik.cucumber.mongodb.rest.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/rest/cucumberplugin")
public class CucumberPluginController
{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoDbFactory dbFactory;

    @Autowired
    private MongoConverter converter;

    @RequestMapping(value = "/{collection}/report", method = RequestMethod.POST)
    public ResponseEntity insertData(@PathVariable("collection") String collection,
            @RequestBody String reportJson)
    {
        mongoTemplate.insert(reportJson, collection);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{collection}/media/{name}", method = RequestMethod.POST)
    public ResponseEntity insertMedia(@PathVariable("collection") String collection,
            @PathVariable(value = "name") String name,
            HttpServletRequest httpServletRequest) throws IOException
    {
        GridFsOperations gridfs = new GridFsTemplate(dbFactory, converter, collection);
        gridfs.store(httpServletRequest.getInputStream(), name);
        return new ResponseEntity(HttpStatus.OK);
    }
}