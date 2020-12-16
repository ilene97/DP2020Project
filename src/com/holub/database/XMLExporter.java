package com.holub.database;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class XMLExporter implements Table.Exporter{
	private final Writer out;
	private 	  int	 width;
	
	private String tableTitle;
	private String[] columnName;

	public XMLExporter( Writer out )
	{	this.out = out;
	}

	public void storeMetadata( String tableName, int width, int height,
							   Iterator columnNames ) throws IOException{	
		this.width = width;
		this.tableTitle = tableName == null ? "anonymous" : tableName;
		this.columnName = new String[width];
		
		out.write("<?xml version=\"1.0\">\n");
		out.write("<root>\n");
		
	    for(int i=0; i<columnName.length; i++)
	    	columnName[i] = columnNames.next().toString();
	}

	public void storeRow( Iterator data ) throws IOException
	{	
		int i = 0;
		out.write("\t<"+tableTitle+">\n");
		while( data.hasNext() )
		{	
			Object datum = data.next();

			if( datum != null )	{
				out.write("\t\t<"+columnName[i]+">");
				out.write( datum.toString() );
				out.write("</"+columnName[i]+">");
				i++;
			}

		}
		out.write("\n\t</"+tableTitle+">\n");
	}

	public void startTable() throws IOException {/*nothing to do*/}
	public void endTable()   throws IOException {
		out.write("</root>\n");
	}
}
