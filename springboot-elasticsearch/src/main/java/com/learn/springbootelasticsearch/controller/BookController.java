package com.learn.springbootelasticsearch.controller;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ES book索引 控制器
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/8/19
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
@Controller
public class BookController {

	@Autowired
	private TransportClient client;

	/**
	 * http://localhost:8080/get/book/novel?id=2
	 * 获取Es中数据
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/get/book/novel")
	@ResponseBody
	public ResponseEntity getBook(@RequestParam("id") String id) {
		if (StringUtils.isEmpty(id)) {
			return new ResponseEntity("ID不能为空！", HttpStatus.NOT_FOUND);
		}
		GetResponse result = client.prepareGet("book", "novel", id).get();

		if (!result.isExists()) {
			return new ResponseEntity("资源没有找到！", HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(result.getSource(), HttpStatus.OK);

	}

	/**
	 * 添加文档数据
	 *  http://localhost:8080/add/book/novel
	 *  form-data
	 * @param title
	 * @param author
	 * @param word_count
	 * @param publish_date
	 * @return
	 */
	@PostMapping(value = "/add/book/novel")
	@ResponseBody
	public ResponseEntity addBook(@RequestParam("title") String title, @RequestParam("author") String author,
			@RequestParam("word_count") Integer word_count,
			@RequestParam("publish_date") String publish_date) {

		try {
			/**
			 * 要加 startObject，否则出现 Can not write a field name, expecting a value 错误！
			 */
			XContentBuilder builder = XContentFactory.jsonBuilder().startObject().field("title", title)
					.field("author", author).field("word_count", word_count)
					.field("publish_date", publish_date).endObject();

			IndexResponse indexResponse = client.prepareIndex("book", "novel").setSource(builder).get();

			return new ResponseEntity(indexResponse.getId(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			new ResponseEntity("内部服务错误！", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity("插入出错！", HttpStatus.INTERNAL_SERVER_ERROR);

	}


	/**
	 * 删除Es中数据
	 * localhost:8080/del/book/novel?id=sdfhUWUB1-6vN1-K8u5f
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/del/book/novel")
	@ResponseBody
	public ResponseEntity delBook(@RequestParam("id") String id) {
		if (StringUtils.isEmpty(id)) {
			return new ResponseEntity("ID不能为空！", HttpStatus.NOT_FOUND);
		}
		DeleteResponse result = client.prepareDelete("book", "novel", id).get();

		return new ResponseEntity(result.toString(), HttpStatus.OK);

	}

	/**
	 * 更新文档
	 *
	 * @param id
	 * @param title
	 * @param author
	 * @param word_count
	 * @param publish_date
	 * @return
	 */
	@PutMapping(value = "/update/book/novel")
	@ResponseBody
	public ResponseEntity updateBook(@RequestParam("id") String id,
			@RequestParam(value = "title",required = false) String title,
			@RequestParam(value = "author",required = false) String author,
			@RequestParam(value = "word_count",required = false) Integer word_count,
			@RequestParam(value = "publish_date",required = false) String publish_date) {
		if (StringUtils.isEmpty(id)) {
			return new ResponseEntity("ID不能为空！", HttpStatus.NOT_FOUND);
		}
		UpdateRequest updateRequest = new UpdateRequest("book","novel",id);

		try {
			XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
			if (!StringUtils.isEmpty(title)){
				builder.field("title", title);
			}
			if (!StringUtils.isEmpty(author)){
				builder.field("author", author);
			}
			if (!StringUtils.isEmpty(word_count)){
				builder.field("word_count", word_count);
			}
			if (!StringUtils.isEmpty(publish_date)){
				builder.field("publish_date", publish_date);
			}

			builder.endObject();

			updateRequest.doc(builder);

			UpdateResponse updateResponse = client.update(updateRequest).get();

			return new ResponseEntity(updateResponse.getId(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("内部错误！", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * localhost:8080/query/book/novel
	 * 复合查询接口
	 * @param title
	 * @param author
	 * @param gt_word_conut
	 * @param lt_word_conut
	 * @return
	 */
	@PostMapping(value = "/query/book/novel")
	@ResponseBody
	public ResponseEntity queryBook(
			@RequestParam(value = "title",required = false) String title,
			@RequestParam(value = "author",required = false) String author,
			@RequestParam(value = "gt_word_conut",defaultValue = "0") int gt_word_conut,
			@RequestParam(value = "lt_word_conut",required = false) Integer lt_word_conut) {


		BoolQueryBuilder boolQuy = QueryBuilders.boolQuery();

		if(!StringUtils.isEmpty(title)){
			boolQuy.must(QueryBuilders.matchQuery("title", title));
		}
		if(!StringUtils.isEmpty(author)){
			boolQuy.must(QueryBuilders.matchQuery("author", author));
		}

		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("word_count").from(gt_word_conut);

		if(lt_word_conut != null && gt_word_conut >0){
			rangeQueryBuilder.to(lt_word_conut);
		}
		boolQuy.filter(rangeQueryBuilder);

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch("book")
				.setTypes("novel")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(boolQuy)
				.setFrom(0)
				.setSize(10);
		SearchResponse searchResponse = searchRequestBuilder.get();
		List<Map<String, Object>> lists = new ArrayList<>();
		for (SearchHit hit:searchResponse.getHits().getHits()) {
			lists.add(hit.getSource());
		}

		return new ResponseEntity(lists,HttpStatus.OK);
	}

}
