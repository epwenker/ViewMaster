package com.epwenker.viewMaster.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ViewMasterModel {

	private String[][] data;
	
	public void saveToFile(String fileName, String url) {
		System.out.println("saveToFile(" + fileName + ", " + url + ")");
		
		if(fileName == null || fileName.trim().isEmpty())
			throw new IllegalArgumentException("Invalid File Name.");
		if(url == null || url.trim().isEmpty())
			throw new IllegalArgumentException("Invalid URL.");
		
		try {
			Document doc = Jsoup.connect(url).get();
			Elements tables = doc.getElementsByTag("Table");
			int tableIndex = 0;
			for (Element table : tables) {
				Elements rows = table.getElementsByTag("TR");
				data = new String[rows.size()][rows.first().getElementsByTag("TH").size()];
				int rowIndex = 0;
				int columnIndex = 0;
				for(Element row : rows) {
					Elements columns = row.children();
					
					columnIndex = 0;
					for(Element column : columns) {						
						int rowSpan = 1;
						if(column.hasAttr("ROWSPAN"))
							rowSpan = Integer.parseInt(column.attr("ROWSPAN"));
						while(data[rowIndex][columnIndex] != null)
							columnIndex++;
						for(int i = rowSpan; i > 0; i--) {
							if(column.textNodes().size() > 1) {
								String text = "";
								
								List<TextNode> textList = column.textNodes();
								Elements links = column.getElementsByTag("A");
								for(Element link : links)
									text += link.ownText() + "    ";
								for(int j = 0; j < textList.size() - 1; j++) {
									text += textList.get(j).toString().replaceAll("&copy;", Character.toString((char) 169)) + "    ";
								}
								text += textList.get(textList.size() - 1).toString().replaceAll("&copy;", Character.toString((char) 169));
								System.out.println(text);
								data[rowIndex + i - 1][columnIndex] = text;
							}
							else
								data[rowIndex + i - 1][columnIndex] = column.text();
						}
						columnIndex++;
					}
					rowIndex++;
				}
				
				String newFileName = fileName;
				if(tableIndex > 0) {
					if(!fileName.endsWith(".csv"))
						fileName += ".csv";
					
					String[] fileNameData = fileName.split("\\.");
					fileNameData[fileNameData.length - 1] = tableIndex + "." + fileNameData[fileNameData.length - 1];
					newFileName = "";
					for(int i = 0; i < fileNameData.length; i++)
						newFileName += fileNameData[i];
				}
				generateCsvFile(newFileName);
				tableIndex++;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void generateCsvFile(String fileName) {
		System.out.println("generateCsvFile(" + fileName + ")");
		try {
			FileWriter writer = new FileWriter(fileName);
			
			if(data == null || data.length == 0 || data[0].length == 0) {
				writer.close();
				throw new IOException();
			}
			
			for(int i = 0; i < data.length; i++) {
				for(int j = 0; j < data[0].length; j++) {
					writer.append("\"" + data[i][j] + "\"");
					writer.append(',');
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
