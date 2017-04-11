package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;  

public class Createindex {
	private static boolean firstfile = false;
	private static boolean onlyindex = true;
	
	public static void main(String[] args) throws IOException{
		String folder = "C:/Documents and Settings/user/My Documents/Downloads/pixiv/";
		//input
		File file = new File(folder);

		File index = new File(folder+"index.html");
		index.createNewFile();
		BufferedWriter indexout = new BufferedWriter(new FileWriter(index));
		indexout.write("<!DOCTYPE html><html><head><meta charset=\"utf-8\">\n");
		indexout.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"index/common.css\">\n");
		indexout.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"index/index.css\">\n");
		indexout.write("<script type=\"text/javascript\" src=\"index/js/jquery.min.js\"></script>\n");
		indexout.write("<script type=\"text/javascript\" src=\"index/js/keywordgrep.js\"></script>\n");
		indexout.write("</head>\n");
		indexout.write("<body>\n");
		indexout.write("<div id=\"index_content\">\n");
		indexout.write("\n");
		indexout.write("<form action=\"#\" onsubmit=\"return false\">\n");
		indexout.write("<input type=\"text\" value=\"\" size=\"30\" id=\"grepword\" accesskey=\"/\" />\n");
		indexout.write("</form>\n");
		indexout.write("\n");
		
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()){				
				File[] htmlfiles = files[i].listFiles();
				if ("assets".equals(files[i].getName())||
						"index".equals(files[i].getName())||
						"novel".equals(files[i].getName())||
						"css".equals(files[i].getName())||
						"fonts".equals(files[i].getName())||
						"js".equals(files[i].getName())||
						"less".equals(files[i].getName())||
						"scss".equals(files[i].getName())){}
				else{
					System.out.println("files[i].getName():" + files[i].getName());
					indexout.write("<div class=\"index_item\">\n");
//					out.write("\t<img class=\"coverimage\" src=\""+img+"\" />\n");
					
					String[] newtags = {};
					HashSet<String> tags = new HashSet();
					//output
					File writename;
					BufferedWriter out = null;
					if (!onlyindex){
						writename = new File(folder+files[i].getName()+".html");
						writename.createNewFile();
						out = new BufferedWriter(new FileWriter(writename));
						out.write("<!DOCTYPE html><html><head><meta charset=\"utf-8\">\n");
						out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"common.css\">\n");
						out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"index.css\">\n");
						out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/font-awesome.min.css\">\n");
						out.write("<script type=\"text/javascript\" src=\"js/jquery.min.js\"></script>\n");
						out.write("<script type=\"text/javascript\" src=\"js/readmore.js\"></script>\n");
						out.write("<script type=\"text/javascript\" src=\"js/keywordgrep.js\"></script>\n");
						out.write("</head>\n");
						out.write("<body>\n");
						out.write("<div id=\"index_content\">\n");
						out.write("\n");
						out.write("<form action=\"#\" onsubmit=\"return false\">\n");
						out.write("<input type=\"text\" value=\"\" size=\"30\" id=\"grepword\" accesskey=\"/\" />\n");
						out.write("</form>\n");
						out.write("\n");
					}
					
					firstfile = true;
					for (int j = 0; j < htmlfiles.length; j++) {
						if (htmlfiles[j].getName().endsWith(".html")){
							newtags = (parseHTML(files[i].getName(), htmlfiles[j], out, indexout));
							
							for (int k = 0; k < newtags.length; k++){
								tags.add(newtags[k]);
							}
						}
					}
					if (!onlyindex){
						out.write("</div>\n");
						out.write("<div class=\"return\"><a href=\"../index.html\"><i class=\"fa fa-chevron-left\" aria-hidden=\"true\"></i></a></div>\n");
						out.write("</body>\n");
						out.write("</html>");
						out.flush();
						out.close();
					}
					
					List<String> list = new ArrayList<String>();
					for(String value : tags){ 
			            list.add(value);
			        }
					Collections.sort(list);
					
					//indexout.write("\t\t<div class=\"tags\">"+list+"</div>\n");
					indexout.write("\t\t<div class=\"tags\">");
					for (int j = 0; j < list.size(); j++){
						indexout.write("<span>"+list.get(j)+"</span>");
					}
					indexout.write("</div>\n");
					indexout.write("\t</div>\n");
					indexout.write("</div>\n");
				}
			}
		}
		
		indexout.write("</div>\n");
		indexout.write("</body>\n");
		indexout.write("</html>");
		indexout.flush();
		indexout.close();
	}
	
	public static String[] parseHTML(String folder, File file,
			BufferedWriter out, BufferedWriter indexout) throws IOException{
//		System.out.println(file.getPath());
//		System.out.println(file.getName());
		Document html = Jsoup.parse(file, "UTF-8");
		
		String filename = file.getName();
		String title = html.select("h1").text();
		String author = html.select("h4").text();
		String date = html.select("#info").text();
		String caption = html.select("#caption").text();
		//String tags = html.select("#tags").text();
		//String[] newtags = tags.split(" ");
		String[] tags = html.select("#tags").text().split(" ");
		String img = "";
//		try{
//			img = html.select("img").first().attr("src");
//		}
//		catch (Exception e){
//			img = "";
//		}
		if ("".equals(author)){
			author = html.select("legend").text();
			try{
				caption = html.select("#caption").get(1).text();
			}
			catch (Exception e){
			}
		}

		if (!onlyindex){
			out.write("<div class=\"index_item\">\n");
		//		out.write("\t<img class=\"coverimage\" src=\""+img+"\" />\n");
		//<a href="javascript:popup('1151895','[7227475]かくべつにあいしてください.html');">
			out.write("\t<a href=\"javascript:popup('"+folder+"','"+filename+"');\">\n" +
					"\t\t<div class=\"title\">"+title+"</div>\n"+
					"\t</a>\n");
			out.write("\t<div class=\"index_info\">\n");
			out.write("\t\t<div class=\"author\">"+author+"</div>\n");
			out.write("\t\t<div class=\"date\">"+date+"</div>\n");
			out.write("\t\t<div class=\"tags\">");
			for (int i = 0; i < tags.length; i++){
				out.write("<span>"+tags[i]+"</span>");
			}
			out.write("</div>\n");
			out.write("\t\t<div class=\"caption\">"+caption+"</div>\n");
			out.write("\t</div>\n");
			out.write("</div>\n");
		}
		
		if (firstfile){
			indexout.write("\t<a href=\"index/"+folder+".html\">\n" +
					"\t\t<div class=\"title\">"+author+"</div>\n"+
					"\t</a>\n");
			indexout.write("\t<div class=\"index_info\">\n");
			firstfile = false;
		}
		indexout.write("\t\t<div class=\"index_titles\">"+
				"<div class=\"index_date\">"+date+"</div>"+
				"<div class=\"index_title\" title=\"");
		for (int i = 0; i < tags.length; i++){
			indexout.write(tags[i] + " ");
		}
		indexout.write("\">"+title+"</div>"+
				"</div>\n");
		
		return tags;
	}
}
