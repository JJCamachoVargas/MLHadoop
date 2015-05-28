import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class thetaMAP extends Mapper<LongWritable, Text, Text, FloatWritable> {
	int num=0;
	Float[] Xi=null;
	ArrayList<Float> theta_i=new ArrayList<Float>();
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		float alpha=Float.parseFloat(context.getConfiguration().get("alpha"));
		int number_inputs=Integer.parseInt(context.getConfiguration().get("number_inputs"));
		float h_theta=0;
		String[] tok=value.toString().split("\\,");
		for(int i=0;i<tok.length;i++){
			theta_i.add(Float.parseFloat(context.getConfiguration().get("theta".concat(String.valueOf(i)))));
		}
		Float[] Xi=new Float[tok.length];
		for(int i=0;i<Xi.length;i++){
			if(i==0){
				Xi[0]=(float) 1;
			}
			else{
				Xi[i]=Float.parseFloat(tok[i-1]);
			}
		}
		for(int i=0;i<Xi.length;i++){
			float exp=0;
			exp+=(Xi[i]*theta_i.get(i));
			//If you choose to use perceptron learning rule
			/*if(i==num_features){
				if(exp>=0){
					h_theta=1;
				}
				else{
					h_theta=0;
				}
			}*/
			//If you choose to use the Logistic Function for learning
			if(i==(Xi.length-1)){
				h_theta=(float) (1/(1+(Math.exp(-(exp)))));
			}
		}
		float Yi=Float.parseFloat(tok[tok.length-1]);
		for(int i=0;i<Xi.length;i++){
			float temp=theta_i.get(i);
			theta_i.remove(i);
			theta_i.add(i,(float) (temp+(alpha/number_inputs)*(Yi-h_theta)*(Xi[i])));
		}
		num=Xi.length;
	}
	@Override
	public void cleanup(Context context) throws IOException, InterruptedException{
		for(int i=0;i<num;i++){ // Don't use theta_i.size() as it doesn't work in Hadoop quite well
			context.write(new Text("theta"+i), new FloatWritable(theta_i.get(i)));
		}
	}
}