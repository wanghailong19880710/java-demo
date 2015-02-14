package com.github.xiaofu.demo.cxf.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.cxf.jaxrs.ext.search.SearchCondition;
import org.apache.cxf.jaxrs.ext.search.SearchContext;

@Path("/books")
@Produces({"text/xml","application/xml","application/json"}) 
public class BooksService {

	private static Map<Long, Book> books;
	@Context
	private SearchContext context;
	static {
		books=new HashMap<Long,Book>();
		Book book1=new Book();
		book1.setId(1);
		book1.setName("t1book");
		Book book2=new Book();
		book2.setId(2);
		book2.setName("t2book");
		books.put(Long.valueOf(1), book1);
		books.put(Long.valueOf(2), book2);
	}
	@GET
	public List<Book> getBook() {
		SearchCondition<Book> sc = context.getCondition(Book.class);
		// SearchCondition#isMet method can also be used to build a list of
		// matching beans

		// iterate over all the values in the books map and return a collection
		// of matching beans
		List<Book> found = sc.findAll(books.values());
		return found;
	}
}