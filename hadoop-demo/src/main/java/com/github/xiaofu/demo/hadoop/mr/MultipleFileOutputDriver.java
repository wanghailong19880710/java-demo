package com.github.xiaofu.demo.hadoop.mr;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class MultipleFileOutputDriver extends Configured implements Tool {

	static class DemoMapper extends Mapper<LongWritable, Text, Text, Text> {

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] fields = value.toString().split(Pattern.quote("|"));
			context.write(new Text(fields[0]), value);
		}
	}

	static class DemoReducer extends
			Reducer<Text, Text, NullWritable, Text> {
		private MultipleOutputs<NullWritable, Text> outputs;

		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			while (values.iterator().hasNext())
			{
				outputs.write("userid", NullWritable.get(), values.iterator().next(),key.toString());
			}
		}

		protected void setup(Context context) throws IOException,
				InterruptedException {
			outputs = new MultipleOutputs<NullWritable, Text>(context);
		}

		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			outputs.close();
		}
	}

	@Override
	public int run(String[] args) throws Exception {
		Job job = Job.getInstance(getConf());
		job.setJobName(this.getClass().getSimpleName());
		job.setJarByClass(this.getClass());
		job.setJar("E:\\open-source-projects\\github\\java-demo-parent\\hadoop-demo\\target\\hadoop-demo-0.0.1-SNAPSHOT.jar");
		job.setMapperClass(DemoMapper.class);
		job.setReducerClass(DemoReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);


		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(NullOutputFormat.class);
		job.setOutputKeyClass(NullWritable.class);
		FileInputFormat.setInputPaths(job, "/user/hive/warehouse/tmp_view_infos/");
		FileOutputFormat.setOutputPath(job, new Path("/data"));
		MultipleOutputs.addNamedOutput(job, "userid", TextOutputFormat.class,
				NullWritable.class, Text.class);
		MultipleOutputs.setCountersEnabled(job, true);
		job.waitForCompletion(true);
		
		return 0;
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("HADOOP_USER_NAME", "vipcloud");
		int exitCode = ToolRunner.run(new MultipleFileOutputDriver(), args);
		System.exit(exitCode);
	}
}
