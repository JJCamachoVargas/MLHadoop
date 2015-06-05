import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class MatMulMap extends
Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		int m = Integer.parseInt(context.getConfiguration().get("m"));
		int n = Integer.parseInt(context.getConfiguration().get("n"));
		int p = Integer.parseInt(context.getConfiguration().get("p"));
		Text Key = new Text();
		Text Value = new Text();
		String line = value.toString();
		String[] val = line.split(",");
		if(val[0]=="A"){
			for(int x=0;x<p;x++){
				String s="";
				Key.set(val[(n+1)]+","+x);
				for(int i=0;i<=n;i++){
					if(i<n){
						s=s.concat(val[i]+",");
					}
					else{
						s=s.concat(val[i]);
					}
				}
				Value.set(s);
				context.write(Key, Value);
			}
		}
		else{
			for(int y=0;y<m;y++){
				String s="";
				Key.set(y+","+val[(n+1)]);
				for(int i=0;i<=n;i++){
					if(i<n){
						s=s.concat(val[i]+",");
					}
					else{
						s=s.concat(val[i]);
					}
				}
				Value.set(s);
				context.write(Key, Value);
			}
		}
	}
}