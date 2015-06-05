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
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 
 * @author xiaofu
 * 
 */
public class MultipleFileOutputDriver extends Configured implements Tool {

	static class DemoMapper extends Mapper<LongWritable, Text, Text, Text> {

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] fields = value.toString().split(Pattern.quote("|"));
			context.write(new Text(fields[0]), value);
		}
	}

	static class DemoReducer extends Reducer<Text, Text, NullWritable, Text> {
		private MultipleOutputs<NullWritable, Text> outputs;

		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			while (values.iterator().hasNext()) {
				// 此方法是使用了当前reduce上下文以及输出配置，只是可以指定输出名而已，它会将临时目录的数据移动到正式目录，因为它是标准输出方式，但是这里会产生空文件，使用lazy包装!
				outputs.write(NullWritable.get(), values.iterator().next(),
						key.toString());
				// 这个另起了一个上下文和指定名称的输出配置，此输出无法将临时目录中的数据移动到正式目录
				// outputs.write("userid", NullWritable.get(),
				// values.iterator().next(),key.toString());
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

		job.setMaxMapAttempts(1);
		job.setMaxReduceAttempts(1);
		job.setInputFormatClass(TextInputFormat.class);
		LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);
		job.setOutputFormatClass(LazyOutputFormat.class);
		job.setOutputKeyClass(NullWritable.class);
		FileInputFormat.setInputPaths(job,
				"/user/hive/warehouse/tmp_view_infos/");
		FileOutputFormat.setOutputPath(job, new Path("/data"));
		MultipleOutputs.addNamedOutput(job, "userid", TextOutputFormat.class,
				NullWritable.class, Text.class);

		job.waitForCompletion(true);

		return 0;
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("HADOOP_USER_NAME", "vipcloud");
		int exitCode = ToolRunner.run(new MultipleFileOutputDriver(), args);
		System.exit(exitCode);
	}
}
