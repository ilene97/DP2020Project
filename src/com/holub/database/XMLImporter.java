package com.holub.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.holub.tools.ArrayIterator;

public class XMLImporter implements Table.Importer {
	private BufferedReader  in;			// null once end-of-file reached
	private String[]        columnNames;
	private String          tableName;

	public XMLImporter( Reader in )
	{	this.in = in instanceof BufferedReader
						? (BufferedReader)in
                        : new BufferedReader(in)
	                    ;
	}
	public void startTable()			throws IOException
	{	
		while(true) {
			if(in.readLine().equals("<root>"))	break;
		}
		
		in.mark(0);
		tableName= in.readLine();
		tableName = tableName.replace("<", "");
		tableName = tableName.replace(">", "");
		tableName = tableName.trim();
		
		String columns = in.readLine().trim();
		String[] test = columns.split("</");
		ArrayList<String> tempColumns = new ArrayList<String>();
		
		for(int i=1; i<test.length; i++) {
			int idx = test[i].indexOf(">");
			tempColumns.add(test[i].substring(0,idx));
		}
		
		columnNames = tempColumns.toArray(new String[tempColumns.size()]);
		//for(int i=0;i<columnNames.length;i++)
		//	System.out.println(columnNames[i]);
		
		in.reset();
		
	}
	public String loadTableName()		throws IOException
	{	return tableName;
	}
	public int loadWidth()			    throws IOException
	{	return columnNames.length;
	}
	public Iterator loadColumnNames()	throws IOException
	{	return new ArrayIterator(columnNames);  //{=CSVImporter.ArrayIteratorCall}
	}

	public Iterator loadRow()			throws IOException
	{	//Iterator row = null;
		
		ArrayList<String> columnValues = new ArrayList<String>();
		
		while(true) {
			String line = in.readLine().trim();
			if(line.equals("<"+tableName+">")) {
				//value들 가져오기
				line = in.readLine().trim();
				line = line.replace(" ","");
				line = line.replaceAll("\\s+", "");
				//System.out.println(line);
				Pattern pattern = Pattern.compile("[>](.*?)[</]");
				Matcher matcher = pattern.matcher(line);
				while(matcher.find()) {
					//System.out.println(matcher.group(1));
					if(matcher.group(1).length()>0)
						columnValues.add(matcher.group(1));
					if(matcher.group(1)==null) break;
				}
			}
			else if(line.equals("</root>")) {
				return null;
			}
			else {
				
			}
			return new ArrayIterator(columnValues.toArray(new String[columnValues.size()]));
		}
		
		/*
		if( in != null )
		{	
			String line = in.readLine();
			
			if( line == null )
				in = null;
			else
				row = new ArrayIterator( line.split("\\s*,\\s*"));
		}
		return row;
		*/
	}

	public void endTable() throws IOException {}
}
