package com.cushon.xword.test;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

import com.cushon.xword.wordsearcher.WordSearcher;

public class WordSearcherTest {

	WordSearcher searcher;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		searcher = new WordSearcher();
		searcher.AddWord("aaaaa");
		searcher.AddWord("bdbab");
		searcher.AddWord("cdcad");
		searcher.AddWord("dddad");
		searcher.AddWord("edcae");
		searcher.AddWord("aae");
		searcher.AddWord("bae");
		searcher.AddWord("cae");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void Sanity0() {
		List<String> result = searcher.getQuery(5, null).Run(); 
		List<String> expected = Arrays.asList("aaaaa", "bdbab", "cdcad", "dddad", "edcae");
		Assert.assertTrue(result.equals(expected));
	}
	
	@Test
	public void Sanity1() {
		List<String> result = searcher.getQuery(5, null).AddParam('a', 0).Run(); 
		List<String> expected = Arrays.asList("aaaaa");
		Assert.assertTrue(result.equals(expected));
	}

	@Test
	public void Sanity2() {
		List<String> result = searcher.getQuery(5, null).AddParam('b', 0).Run(); 
		List<String> expected = Arrays.asList("bdbab");
		Assert.assertTrue(result.equals(expected));
	}
	
	@Test
	public void Sanity3() {
		List<String> result = searcher.getQuery(5, null).AddParam('d', 1).Run(); 
		List<String> expected = Arrays.asList("bdbab", "cdcad", "dddad", "edcae");
		Assert.assertTrue(result.equals(expected));
	}
	
	@Test
	public void Sanity4() {
		List<String> result = searcher.getQuery(5, null).AddParam('d', 1).AddParam('d', 4).Run(); 
		List<String> expected = Arrays.asList("cdcad", "dddad");
		Assert.assertTrue(result.equals(expected));
	}
	
	@Test
	public void Sanity5() {
		List<String> result = searcher.getQuery(3, null).AddParam('e', 2).Run(); 
		List<String> expected = Arrays.asList("aae", "bae", "cae");
		Assert.assertTrue(result.equals(expected));
	}
	
	@Test
	public void Sanity6() {
		List<String> result = searcher.getQuery(3, null).Run(); 
		List<String> expected = Arrays.asList("aae", "bae", "cae");
		Assert.assertTrue(result.equals(expected));
	}
	
	@Test
	public void Sanity7() {
		List<String> result = searcher.getQuery(3, null).AddParam('a', 1).Run(); 
		List<String> expected = Arrays.asList("aae", "bae", "cae");
		Assert.assertTrue(result.equals(expected));
	}
}
