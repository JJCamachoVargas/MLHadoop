import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Map extends Mapper<LongWritable, Text, Text, Text> {
	long byteoffset=0;
	String species=null;
	ArrayList<String> dists=new ArrayList<String>();
	float min_dist=0;
	public float euc_dist(Float[] feat, Float[] test,int num){
		float distance=0;
		float val=0;
		for(int i=0;i<num;i++){
			val+=((feat[i]-test[i])*(feat[i]-test[i]));
		}
		distance=(float) Math.sqrt(val);
		return distance;
	}
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] characteristics=value.toString().split("\\ ");
		int num_features=characteristics.length-1;
		Float[] feat=new Float[num_features];
		Float[] test=new Float[num_features];
		for(int i=0;i<num_features;i++){
			feat[i]=Float.parseFloat(context.getConfiguration().get("feat"+i));
		}

		for(int i=0;i<num_features;i++){
			test[i]=Float.parseFloat(characteristics[i]);
		}
		species=characteristics[num_features].replace("\"", "");
		dists.add(String.valueOf(euc_dist(feat,test,num_features))+species);
		byteoffset=Long.parseLong(key.toString());
	}
	@Override
	public void cleanup(Context context) throws IOException, InterruptedException{
		Collections.sort(dists);
		int iter=0;
		String[] species=new String[3];
		String str="";
		for(int i=0;i<3;i++){
			str=dists.get(i);
			String spec=String.valueOf(str.replaceAll("[\\d.]", ""));
			species[iter]=spec;
			iter++;
		}
		Arrays.sort(species);
		for(int i=0;i<species.length-1;i++){
			if(species[i].equals(species[i+1])){
				context.write(new Text("1"), new Text(species[i]));
				break;
			}
		}
	}
}
