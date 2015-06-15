import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class thetaMAP extends Mapper<LongWritable, Text, Text, FloatWritable> {
	public static int num=0, count=0, number_inputs=0;
	public static float alpha=0.0f;
	public static Float[] Xi=null;
	public static ArrayList<Float> theta_i=new ArrayList<Float>();
	@Override
	public void setup(Context context) throws IOException, InterruptedException{
		alpha=context.getConfiguration().getFloat("alpha"),
		number_inputs=context.getConfiguration().getInt("number_inputs");
	}
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		++count;
		float h_theta=0;
		String[] tok=value.toString().split("\\,");
		if(count==1){
			for(int i=0;i<tok.length;i++){
				theta_i.add(context.getConfiguration().getFloat("theta".concat(String.valueOf(i))));
			}
			Xi=new Float[tok.length];
		}
		for(int i=0;i<Xi.length;i++){
			if(i==0){
				Xi[0]=(float) 1;
			}
			else{
				Xi[i]=Float.parseFloat(tok[i-1]);
		}
		}
		for(int i=0;i<Xi.length;i++){
			h_theta+=(float) (Xi[i]*theta_i.get(i));
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
