/*
 * User: joel
 * Date: 2020-01-08
 * Time: 21:49
 */
package se.caglabs.cloudracing.common.persistence.stuff;

import java.util.HashMap;

/**
 *
 */
public class CorsHeaders extends HashMap<String, String> {
  {
    put("Access-Control-Allow-Origin", "*");
    put("Access-Control-Allow-Methods", "GET,OPTIONS");
  }
}
