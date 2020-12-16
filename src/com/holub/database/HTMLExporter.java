package com.holub.database;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class HTMLExporter implements Table.Exporter{
    private final Writer out;
    private int width;

    public HTMLExporter( Writer out )
    {	this.out = out;
    }

    public void storeMetadata( String tableName, int width, 
    		int height, Iterator columnNames ) throws IOException{
        this.width = width;
        out.write("<!DOCTYPE html>\n");
        out.write("<html>\n");
        out.write("<head>\n");
        out.write("\t<title>");
        out.write(tableName == null ? "anonymous" : tableName );
        out.write("</title>\n");
        out.write("</head>\n");
        out.write("<body>\n");
        out.write("\t<table border=\"1\">\n");
        storeRow( columnNames );
    }

    public void storeRow( Iterator data ) throws IOException
    {	
    	 String rowData = "";
    	 out.write("\t<tr>\n");
         while( data.hasNext() )
         {   
            Object datum = data.next();
            out.write("\t\t<td>");

            if( datum != null )   
               out.write(datum.toString()+"</td>\n");
         }
         out.write("\t</tr>\n");
    }

    public void startTable() throws IOException {/*nothing to do*/}
    public void endTable()   throws IOException {
    	out.write("\t</table>\n");
    	out.write("</body>\n");
    	out.write("</html>\n");
    }
}
