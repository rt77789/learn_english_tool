package com.alibaba.prt.segment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.alibaba.prt.struct.Pair;
import com.alibaba.prt.struct.Token;
import com.alibaba.prt.util.Configuration;

/**
 * ���α任��Ŀǰʵ���˸���䵥��Ĵ���.
 * 
 * @author aliguagua.zhengy
 * 
 */
class Morpher {
	private static Morpher morpher = new Morpher();

	private Map<String, String> irregularMap = new HashMap<String, String>();
	private Set<String> standardSpelling = new HashSet<String>();
	private List<Pair<String, String>> regularRule = new ArrayList<Pair<String, String>>();

	private Morpher() {
		Configuration config = Configuration.getInstance();
		String dir = config.getValueStr("morph_dir");

		loadRegularList(dir + File.separator + "regular.list");
		loadIrregularList(dir + File.separator + "irregular.list");
		loadStandardSpellingList(dir + File.separator
				+ "standardspellings.list");
	}

	public static Morpher getInstance() {
		return morpher;
	}

	/**
	 * ���ر任�����ļ�
	 * 
	 * @param fileName
	 */
	private void loadRegularList(String fileName) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(new File(fileName)));
			String line;
			while ((line = reader.readLine()) != null) {
				Scanner scan = new Scanner(line);
				List<String> rule = new ArrayList<String>();

				while (scan.hasNext()) {
					rule.add(scan.next());
				}

				if (rule.size() < 2) {
					rule.add("");
				}

				assert (rule.size() == 2);

				regularRule.add(new Pair<String, String>(rule.get(0), rule
							.get(1)));
			}

			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ز�����ʴʵ�.
	 * 
	 * @param fileName
	 */
	private void loadIrregularList(String fileName) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(new File(fileName)));
			String line;
			while ((line = reader.readLine()) != null) {
				Scanner scan = new Scanner(line);
				List<String> rule = new ArrayList<String>();

				while (scan.hasNext()) {
					rule.add(scan.next());
				}

				if (rule.size() >= 2) {
					String pluralWord = rule.get(0).toLowerCase();
					String protoWord = rule.get(1).toLowerCase();

					irregularMap.put(pluralWord, protoWord);
				}

				assert (rule.size() == 2);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ر�׼ƴд�ʵ�.
	 * 
	 * @param fileName
	 */
	private void loadStandardSpellingList(String fileName) {
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(new File(fileName)));
			String line;
			while ((line = reader.readLine()) != null) {
				standardSpelling.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �Ƿ��ܹ�����䵥��任.
	 * 
	 * @param noun
	 * @return
	 */
	private boolean canConvert(String noun) {
		return true;
	}

	/**
	 * ����䵥��任.
	 * 
	 * @param token
	 * @return
	 */
	public String convert(String token) {
		if (!canConvert(token))
			return token;

		if (isIrregular(token)) {
			return convertByIrregular(token);
		}

		String lcToken = token.toLowerCase();

		if (isIrregular(lcToken)) {
			return convertByIrregular(lcToken);
		}

		if (isRegular(token)) {
			String res = convertByRegular(token);
			if (standardSpelling.contains(res))
				return res;
		}

		if (isRegular(lcToken)) {
			String res = convertByRegular(lcToken);
			if (standardSpelling.contains(res))
				return res;
		}

		return token;
	}

	/**
	 * �Ƿ��ǲ������.
	 * 
	 * @param token
	 * @return
	 */
	private boolean isIrregular(String token) {
		return irregularMap.containsKey(token);
	}

	/**
	 * �Ƿ��ǹ����.
	 * 
	 * @param token
	 * @return
	 */
	private boolean isRegular(String token) {
		return true;
	}

	/**
	 * ʹ�ò�����ʵ���б任.
	 * 
	 * @param token
	 * @return
	 */
	private String convertByIrregular(String token) {
		String res = irregularMap.get(token);
		assert (res != null);
		return res;
	}

	/**
	 * ʹ�ù���任����һ������Ĺ���ͷ���.
	 * 
	 * @param token
	 * @return
	 */
	private String convertByRegular(String token) {
		String res = token;
		char[] tc = token.toCharArray();

		for (int i = 0; i < regularRule.size(); ++i) {
			char[] pattern = regularRule.get(i).first.toCharArray();
			String single = regularRule.get(i).second;

			int tIndex = token.length() - 1;
			int pIndex = pattern.length - 1;
			int xnum = 0;

			while (tIndex >= 0 && pIndex >= 0) {
				if (pattern[pIndex] != '.') {
					if (pattern[pIndex] != tc[tIndex])
						break;
				} else {
					++xnum;
				}
				--tIndex;
				--pIndex;
			}
			if (pIndex < 0) {
				res = token.substring(0, tIndex + 1 + xnum) + single;
				if (standardSpelling.contains(res))
					return res;
				// return res;
			}
		}
		// / here returns the token. not res.
		return res;
	}

	/* Nouns ending in s, z, x, sh, and ch form the plural by adding - es. */
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scan = new Scanner(System.in);

		EnglishSegment seg = EnglishSegment.getInstance();

		while (scan.hasNext()) {
			String line = scan.nextLine();

			List<Token> tokens = seg.wordSegment(line);

			for (int i = 0; i < tokens.size(); ++i) {
				System.out.print("[" + tokens.get(i).getWord() + " | "
						+ tokens.get(i).getProtoWord() + "]");
			}

			System.out.println();
		}
	}
}

