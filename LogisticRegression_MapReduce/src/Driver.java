import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Driver {
	public static int number_inputs; // needs to be set
	public static int num_features; // needs to be set
	public static float alpha; // needs to be set
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException{
		//args[0] is the total number of inputs the input file or files will have.
		//can be an approximate round number.
		number_inputs=Integer.parseInt(args[0]);
		//args[1] is the number of features each input has.
		num_features=Integer.parseInt(args[1]);
		//args[2] is the value of alpha that you want to use.
		alpha=Float.parseFloat(args[2]);
		Configuration conf=new Configuration();
		FileSystem hdfs=FileSystem.get(conf);
		Float[] theta=new Float[num_features];
		//args[3] is the number of times you want to iterate over your training set.
		for(int i=0;i<Integer.parseInt(args[3]);i++){
			//for the first run
			if(i==0){
				for(int i1=0;i1<num_features;i1++){
					theta[i1]=(float) 0;
				}
			}
			//for the second run
			else{
				int iter=0;
				//args[5] is the output path for storing the theta values.
				BufferedReader br1 = new BufferedReader(new InputStreamReader(hdfs.open(new Path(args[5]))));
				String line1=null;
				while((line1=br1.readLine())!=null){
					String[] theta_line=line1.toString().split("\t");
					theta[iter]=Float.parseFloat(theta_line[1]);
					iter++;
				}
				br1.close();
			}
			if(hdfs.exists(new Path("/home/punit/Desktop/theta_out"))){
				hdfs.delete(new Path("/home/punit/Desktop/theta_out"),true);
			}
			//alpha value initialisation
			conf.setFloat("alpha", alpha);
			//Theta Value Initialisation
			for(int j=0;j<num_features;j++){
				conf.setFloat("theta".concat(String.valueOf(j)), theta[j]);
			}
			//Setting the number of inputs
			conf.setInt("number_inputs", number_inputs);
			Job job = new Job(conf,"Calculation of Theta");
			job.setJarByClass(Driver.class);
			//args[4] is the input path.
			FileInputFormat.setInputPaths(job, new Path(args[4]));
			FileOutputFormat.setOutputPath(job, new Path(args[5]));;
			job.setMapperClass(thetaMAP.class);
			job.setReducerClass(thetaREDUCE.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(FloatWritable.class);
			job.waitForCompletion(true);
		}
	}  
}
