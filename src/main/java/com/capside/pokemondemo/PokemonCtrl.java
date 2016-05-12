/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capside.pokemondemo;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ciber
 */
@Controller
@Scope(scopeName = "singleton")
@Log
public class PokemonCtrl {

    private final ConfigurableApplicationContext ctx;
    private final Pokemon pokemon;

    @Autowired
    public PokemonCtrl(PokemonRepository repository, ApplicationContext ctx) {
        this.pokemon = repository.getRandomPokemon();
        this.ctx = (ConfigurableApplicationContext) ctx;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String index(Map<String, Object> model) {
        String hostname = System.getenv("HOSTNAME") == null ? "" : System.getenv("HOSTNAME");
        model.put("container", hostname);
        model.put("pokemon", pokemon);
        
        Set<Map.Entry<String,String>> env = System.getenv().entrySet();
        model.put("env", env);
        
        return "index"; 
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    @ResponseBody
    Pokemon shutdown() {
        log.warning(MessageFormat.format("{0} doesn''t want to fight, leaves the room now.", pokemon.getName()));
        new Thread(new Runnable() { 
            @Override @SneakyThrows
            public void run() { 
                Thread.sleep(1000);
                ctx.close(); 
            }
        }).start();
        return this.pokemon;
    }

}
