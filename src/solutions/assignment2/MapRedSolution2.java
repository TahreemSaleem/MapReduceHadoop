package solutions.assignment2;

import examples.MapRedFileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapRedSolution2 {

    public static class MapRecords extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text time = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {


            String line = value.toString();
            String[] attributes = line.split(",");
            Pattern pattern = Pattern.compile("\\b([01][0-9]|2[0-3]):\\b", Pattern.CASE_INSENSITIVE);
            Matcher pickup = pattern.matcher(attributes[1]);
            Matcher dropOff = pattern.matcher(attributes[2]);
            boolean matchpickup = pickup.find();
            boolean matchdropoff = dropOff.find();

            if (matchpickup && matchdropoff) {
                String hourKey = null;
                int matchedPickUp = Integer.parseInt(pickup.group(1));
                int matchedDropOff = Integer.parseInt(dropOff.group(1));
                if ((matchedPickUp) == 0) {
                    hourKey = "12am";
                } else if ((matchedPickUp) == 12) {
                    hourKey = "12pm";
                } else {
                    hourKey = (matchedPickUp) % 12 + "" + (((matchedPickUp) >= 12) ? "pm" : "am");
                }
                time.set(hourKey);
                context.write(time, one);

//                if (matchedDropOff >= matchedPickUp) {
//                    int diff = matchedDropOff - matchedPickUp;
//                    for (int i = 0; i <= diff; i++) {
//                        if ((matchedPickUp + i) == 0) {
//                            hourKey = "12am";
//                        }
//                        else if ((matchedPickUp + i) == 12) {
//                            hourKey = "12pm";
//                        }
//                        else {
//                            hourKey = (matchedPickUp + i) % 12 + "" + (((matchedPickUp + i) >= 12) ? "pm" : "am");
//                        }
////                        System.out.println("1 "+hourKey);
//                        time.set(hourKey);
//                        context.write(time, one);
//                    }
//                } else {
//                    int diff1 = 24 - matchedPickUp;
//                    for (int i = 0; i < diff1; i++) {
//                        if ((matchedPickUp + i) == 0) {
//                            hourKey = "12am";
//                        }
//                        else if ((matchedPickUp + i) == 12) {
//                            hourKey = "12pm";
//                        }else {
//                            hourKey = (matchedPickUp + i) % 12 + "" + (((matchedPickUp + i) >= 12) ? "pm" : "am");
//                        }//                        System.out.println("2 "+hourKey);
//                        time.set(hourKey);
//                        context.write(time, one);
//                    }
//                    for (int i = 0; i <= matchedDropOff; i++) {
//
//                        if (i == 0) {
//                            hourKey = "12am";
//                        }else if (i == 12) {
//                            hourKey = "12pm";
//                        } else {
//                            hourKey = (i) % 12 + "" + (((i) >= 12) ? "pm" : "am");
////                            System.out.println("3 " + hourKey);
//                        }
//                        time.set(hourKey);
//                        context.write(time, one);
//                    }
//                }
            }
        }
    }

    public static class ReduceRecords extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values,
                              Context context) throws IOException, InterruptedException {
            int sum = 0;

            for (IntWritable val : values)
                sum += val.get();

            result.set(sum);
            context.write(key, result);
        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        String[] otherArgs =
                new GenericOptionsParser(conf, args).getRemainingArgs();

        if (otherArgs.length != 2) {
            System.err.println("Usage: MapRedSolution2 <in> <out>");
            System.exit(2);
        }

        Job job = Job.getInstance(conf, "MapRed Solution #2");

        job.setInputFormatClass(TextInputFormat.class);

        job.setMapperClass(MapRedSolution2.MapRecords.class);
        job.setCombinerClass(MapRedSolution2.ReduceRecords.class);
        job.setReducerClass(MapRedSolution2.ReduceRecords.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

        MapRedFileUtils.deleteDir(otherArgs[1]);
        int exitCode = job.waitForCompletion(true) ? 0 : 1;

        FileInputStream fileInputStream = new FileInputStream(new File(otherArgs[1] + "/part-r-00000"));
        String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fileInputStream);
        fileInputStream.close();

        String[] validMd5Sums = {"03357cb042c12da46dd5f0217509adc8", "ad6697014eba5670f6fc79fbac73cf83", "07f6514a2f48cff8e12fdbc533bc0fe5",
                "e3c247d186e3f7d7ba5bab626a8474d7", "fce860313d4924130b626806fa9a3826", "cc56d08d719a1401ad2731898c6b82dd",
                "6cd1ad65c5fd8e54ed83ea59320731e9", "59737bd718c9f38be5354304f5a36466", "7d35ce45afd621e46840627a79f87dac"};

        for (String validMd5 : validMd5Sums) {
            if (validMd5.contentEquals(md5)) {
                System.out.println("The result looks good :-)");
                System.exit(exitCode);
            }
        }
        System.out.println("The result does not look like what we expected :-(");
        System.exit(exitCode);
    }
}
