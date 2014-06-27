package com.epwenker.viewMaster.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewMasterModel {

	private ArrayList<String[][]> data = new ArrayList<String[][]>();
	
	public void saveToFile(String fileName, String url) {
		// check validity of filename and url
		if(fileName == null || fileName.trim().isEmpty())
			throw new IllegalArgumentException("Invalid File Name.");
		if(url == null || url.trim().isEmpty())
			throw new IllegalArgumentException("Invalid URL.");
		
		// parsing
		try {
			// clean html
			Document doc = Jsoup.connect(url).get();
			Element body = doc.getElementsByTag("body").get(0);
			String html = body.toString();
			html = Jsoup.clean(html, Whitelist.none().addTags("table", "tr", "th", "td", "br").addAttributes(":all", "rowspan"));
			doc = Jsoup.parse(html);
			
			
			Elements tables = doc.getElementsByTag("table");
			for (Element table : tables) {
				
				Elements rows = table.getElementsByTag("TR");
				
				int maxCols = 0;
				for(Element row : rows)
					if(row.children().size() > maxCols)
						maxCols = row.children().size();
				
				String[][] dataArray = new String[rows.size()][maxCols];
				int rowIndex = 0;
				for(Element row : rows) {
					Elements cells = row.children();
					int cellIndex = 0;
					for(Element cell : cells) {				
						int rowSpan = 1;
						if(cell.hasAttr("ROWSPAN"))
							rowSpan = Integer.parseInt(cell.attr("ROWSPAN"));
						if(rowSpan > dataArray.length - rowIndex)
							rowSpan = dataArray.length - rowIndex; // make sure rowspan doesn't exceed number of rows
							
						while(dataArray[rowIndex][cellIndex] != null) // don't overwrite filled-in cells
							cellIndex++;
						
						// get cell's text
						String text = "";
						List<TextNode> textList = cell.textNodes();
						for(int j = 0; j < textList.size() - 1; j++) {
							if(!textList.get(j).toString().trim().isEmpty())
								text += textList.get(j).toString() + "   //   ";
						}
						if(!textList.get(textList.size() - 1).toString().trim().isEmpty()) // if last text piece isn't empty, append without following "   //   "
							text += textList.get(textList.size() - 1).toString();
						else
							if(text.length() >= 8) // if last text piece is empty, remove last "   //   " from text
								text = text.substring(0, text.length() - 8);
						
						// replace html codes with regular characters
						text = replaceHtmlChars(text);
						
						// clear "   //   " from top left cell ("Reel number" cell)
						if(rowIndex == 0 && cellIndex == 0)
							text = text.replaceAll("   //   ", " ");
						
						for(int i = rowSpan; i > 0; i--) {
							dataArray[rowIndex + i - 1][cellIndex] = text;
						}
						cellIndex++;
					}
					rowIndex++;
				}
				data.add(dataArray);
			}
			
			// add .csv to filename
			if(!fileName.endsWith(".csv"))
				fileName += ".csv";
			generateCsvFile(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String replaceHtmlChars(String str) {
		str = str.replaceAll("&amp;", "&");
		str = str.replaceAll("&quot;", "'");
		
		str = str.replaceAll("&copy;", Character.toString((char) 169));
		str = str.replaceAll("&nbsp;", " ");
		
		str = str.replaceAll("&Agrave;", Character.toString((char) 192));
		str = str.replaceAll("&Aacute;", Character.toString((char) 193));
		str = str.replaceAll("&Acirc;", Character.toString((char) 194));
		str = str.replaceAll("&Atilde;", Character.toString((char) 195));
		str = str.replaceAll("&Auml;", Character.toString((char) 196));
		str = str.replaceAll("&Aring;", Character.toString((char) 197));
		str = str.replaceAll("&AElig;", Character.toString((char) 198));
		str = str.replaceAll("&Ccedil;", Character.toString((char) 199));
		str = str.replaceAll("&Egrave;", Character.toString((char) 200));
		str = str.replaceAll("&Eacute;", Character.toString((char) 201));
		str = str.replaceAll("&Ecirc;", Character.toString((char) 202));
		str = str.replaceAll("&Euml;", Character.toString((char) 203));
		str = str.replaceAll("&Igrave;", Character.toString((char) 204));
		str = str.replaceAll("&Iacute;", Character.toString((char) 205));
		str = str.replaceAll("&Icirc;", Character.toString((char) 206));
		str = str.replaceAll("&Iuml;", Character.toString((char) 207));
		
		str = str.replaceAll("&ETH;", Character.toString((char) 208));
		str = str.replaceAll("&Ntilde;", Character.toString((char) 209));
		str = str.replaceAll("&Ograve;", Character.toString((char) 210));
		str = str.replaceAll("&Oacute;", Character.toString((char) 211));
		str = str.replaceAll("&Ocirc;", Character.toString((char) 212));
		str = str.replaceAll("&Otilde;", Character.toString((char) 213));
		str = str.replaceAll("&Ouml;", Character.toString((char) 214));
		str = str.replaceAll("&times;", Character.toString((char) 215));
		str = str.replaceAll("&Oslash;", Character.toString((char) 216));
		str = str.replaceAll("&Ugrave;", Character.toString((char) 217));
		str = str.replaceAll("&Uacute;", Character.toString((char) 218));
		str = str.replaceAll("&Ucirc;", Character.toString((char) 219));
		str = str.replaceAll("&Uuml;", Character.toString((char) 220));
		str = str.replaceAll("&Yacute;", Character.toString((char) 221));
		str = str.replaceAll("&THORN;", Character.toString((char) 222));
		str = str.replaceAll("&szlig;", Character.toString((char) 223));
		
		str = str.replaceAll("&agrave;", Character.toString((char) 224));
		str = str.replaceAll("&aacute;", Character.toString((char) 225));
		str = str.replaceAll("&acirc;", Character.toString((char) 226));
		str = str.replaceAll("&atilde;", Character.toString((char) 227));
		str = str.replaceAll("&auml;", Character.toString((char) 228));
		str = str.replaceAll("&aring;", Character.toString((char) 229));
		str = str.replaceAll("&aelig;", Character.toString((char) 230));
		str = str.replaceAll("&ccedil;", Character.toString((char) 231));
		str = str.replaceAll("&egrave;", Character.toString((char) 232));
		str = str.replaceAll("&eacute;", Character.toString((char) 233));
		str = str.replaceAll("&ecirc;", Character.toString((char) 234));
		str = str.replaceAll("&euml;", Character.toString((char) 235));
		str = str.replaceAll("&igrave;", Character.toString((char) 236));
		str = str.replaceAll("&iacute;", Character.toString((char) 237));
		str = str.replaceAll("&icirc;", Character.toString((char) 238));
		str = str.replaceAll("&iuml;", Character.toString((char) 239));
		
		str = str.replaceAll("&eth;", Character.toString((char) 240));
		str = str.replaceAll("&ntilde;", Character.toString((char) 241));
		str = str.replaceAll("&ograve;", Character.toString((char) 242));
		str = str.replaceAll("&oacute;", Character.toString((char) 243));
		str = str.replaceAll("&ocirc;", Character.toString((char) 244));
		str = str.replaceAll("&otilde;", Character.toString((char) 245));
		str = str.replaceAll("&ouml;", Character.toString((char) 246));
		str = str.replaceAll("&divide;", Character.toString((char) 247));
		str = str.replaceAll("&oslash;", Character.toString((char) 248));
		str = str.replaceAll("&ugrave;", Character.toString((char) 249));
		str = str.replaceAll("&uacute;", Character.toString((char) 250));
		str = str.replaceAll("&ucirc;", Character.toString((char) 251));
		str = str.replaceAll("&uuml;", Character.toString((char) 252));
		str = str.replaceAll("&yacute;", Character.toString((char) 253));
		str = str.replaceAll("&thorn;", Character.toString((char) 254));
		str = str.replaceAll("&yuml;", Character.toString((char) 255));
		
		return str;
	}
	
	private void generateCsvFile(String fileName) {
		try {
			FileWriter writer = new FileWriter(fileName);
			
			if(data == null || data.size() == 0) {
				writer.close();
				throw new IOException();
			}
			
			for(int i = 0; i < data.size(); i++) {
				
				if(data.get(i).length == 0) {
					writer.close();
					throw new IOException();
				}
				
				for(int j = 0; j < data.get(i).length; j++) {
					
					if(data.get(i)[j].length == 0) {
						writer.close();
						throw new IOException();
					}
					
					for(int k = 0; k < data.get(i)[j].length; k++) {
						if(data.get(i)[j][k] == null)
							data.get(i)[j][k] = "";
						writer.append("\"" + data.get(i)[j][k] + "\"");
						writer.append(',');
					}
					writer.append('\n');
				}
				writer.append('\n');
			}
			
			writer.flush();
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
