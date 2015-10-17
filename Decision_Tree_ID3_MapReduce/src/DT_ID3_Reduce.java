import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/* NOTE!!: Right now I have just written all the trees that are generated on the various distributed data blocks.
	   I Did not add the code for sort of averaging out all the decision trees and select only one tree because
	   I did not have time.
*/
public class DT_ID3_Reduce extends Reducer<Text, Text, Text, Text>{
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
			for(Text value:values){
				context.write(null, value);
			}
	}
}
