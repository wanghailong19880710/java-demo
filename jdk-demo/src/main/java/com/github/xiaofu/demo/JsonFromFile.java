/**
 * 
 */
package com.github.xiaofu.demo;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author fulaihua
 *
 */

 

public class JsonFromFile {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		LineNumberReader read = new LineNumberReader(
				new FileReader(
						"E:\\VIP\\spider\\dataCrawler\\QkParser1.0\\QkParser1.0\\file\\fulaihua\\2015\\ref.txt"));
		String line = read.readLine();
		while (line != null) {
			ObjectMapper mapper = new ObjectMapper();
			Ref ref = mapper.readValue(line, Ref.class);
			//JsonNode root = mapper.readTree(line);
			System.out.println(mapper.writeValueAsString(ref));
			line = read.readLine();
		}
	}

}
