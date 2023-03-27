// import necessary packages
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Longest_Common_Extension {
	// to handle exceptions include throws
	public static void main(String[] args) throws IOException {

		System.out.print("Please enter first file:");
		Scanner input1 = new Scanner(System.in);
		String fileName1 = input1.nextLine();

		System.out.print("Please enter second file:");
		Scanner input2 = new Scanner(System.in);
		String fileName2 = input2.nextLine();

		System.out.print("Please enter first start point (Indices start with 0):");
		Scanner input3 = new Scanner(System.in);
		int start = input3.nextInt();

		System.out.print("Please enter second start point (Indices start with 0):");
		Scanner input4 = new Scanner(System.in);
		int end = input4.nextInt();

		char[] array1 = readFile(fileName1); // obtain the array
		char[] array2 = readFile(fileName2); // obtain the array

		int first = directCompare(array1, array2, start - 1, end - 1);

		System.out.print("Compare the texts " + fileName1 + " and " + fileName2 + " starting from positions " + start
				+ " and " + end + ": (first method) ");
		System.out.println(first);

		String[] arr1 = buildSuffix(array1);
		String[] arr2 = buildSuffix(array2);

		// Merge
		String[] arr3 = merge(arr1, arr2);

		// Suffixes after sorting
		String[] arr4 = sort(arr3);

		// LCP
		int[] lcp = new int[arr4.length];
		lcp = computeLcp(arr4);

		// Suffix Array
		String[] suffixArray = buildSuffixArray(arr1, arr2, arr4);

		// Answer
		int answer = min(start, end, suffixArray, lcp);

		System.out.println("");
		System.out.println("SA");
		System.out.println(Arrays.toString(suffixArray));
		System.out.println("");

		System.out.println("Suffix SA");
		System.out.println(Arrays.toString(arr4));
		System.out.println("");

		System.out.println("LCP");
		System.out.println(Arrays.toString(lcp));
		System.out.println("");

		System.out.print("Answer: (second method) ");
		System.out.println(answer);

	}

	public static char[] readFile(String fileName) throws IOException {
		// list that holds strings of a file
		List<String> listOfStrings = new ArrayList<String>();

		// load data from file
		BufferedReader bf = new BufferedReader(new FileReader(fileName));

		// read entire line as string
		String line = bf.readLine();

		// checking for end of file
		while (line != null) {
			listOfStrings.add(line);
			line = bf.readLine();
		}

		// closing bufferreader object
		bf.close();

		// storing the data in arraylist to array
		String[] array = listOfStrings.toArray(new String[0]);
		char[] array2 = convert(array);
		return array2;
	}

	public static char[] convert(String[] arr) {
		StringBuilder sb = new StringBuilder();
		for (String s : arr) {
			sb.append(s);
		}
		return sb.toString().toCharArray();
	}

	public static int directCompare(char[] array1, char[] array2, int i, int j) {
		int t = 0;

		while ((array1[i + t] == array2[j + t]) && (i + t <= array1.length) && (j + t <= array2.length)) {
			t = t + 1;
		}

		return t;

	}

	public static String[] buildSuffix(char[] arr) {

		int size = arr.length;
		String[] suf = new String[size];

		for (int i = 0; i < size; i++) {

			// String temp = arr.toString();
			String temp = new String(arr);

			suf[i] = temp.substring(size - i - 1);
		}

		return suf;

	}

	public static String toString(char[] a) {
		// Creating object of String class
		StringBuilder sb = new StringBuilder();

		// Creating a string using append() method
		for (int i = 0; i < a.length; i++) {
			sb.append(a[i]);
		}

		return sb.toString();
	}

	public static String[] merge(String[] arr1, String[] arr2)

	{

		int size1 = arr1.length;
		int size2 = arr2.length;
		int totalSize = size1 + size2;

		// create the resultant array
		String[] mergedArr = new String[totalSize];

		System.arraycopy(arr1, 0, mergedArr, 0, size1);
		System.arraycopy(arr2, 0, mergedArr, size1, size2);

		return mergedArr;

	}

	public static String[] sort(String[] arr) {
		int size = arr.length;

		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[i].compareTo(arr[j]) > 0) {
					String temp = arr[i];
					arr[i] = arr[j];
					arr[j] = temp;
				}
			}
		}
		return arr;

	}

	public static int[] computeLcp(String[] arr) {

		int j = 1;

		int[] result = new int[arr.length];
		result[0] = 0;

		for (int i = 0; i < arr.length - 1; i++) {

			int lcpValue = 0;

			char[] temp1 = convertToChar(arr[i]);
			char[] temp2 = convertToChar(arr[j]);

			// System.out.println(directCompare(temp1,temp2, 0 , 0));

			j++;
			int k;
			for (k = 0; (k < temp1.length) && (k < temp2.length); k++) {
				if (Character.compare(temp1[k], temp2[k]) == 0) {
					lcpValue++;

					result[i + 1] = lcpValue;

				} else {
					result[i + 1] = lcpValue;
					break;
				}
			}

		}
		return result;

	}

	public static char[] convertToChar(String str) {

		char[] ch = new char[str.length()];

		for (int i = 0; i < str.length(); i++) {
			ch[i] = str.charAt(i);
		}
		return ch;

	}

	public static String[] buildSuffixArray(String[] arr1, String[] arr2, String[] merged) {

		String[] suffixArray = new String[merged.length];

		String[] sa1 = new String[arr1.length];
		String[] sa2 = new String[arr2.length];

		for (int i = 0; i < arr1.length; i++) {
			sa1[i] = arr1.length - i + "(1)";

		}

		for (int i = 0; i < arr2.length; i++) {
			sa2[i] = arr2.length - i + "(2)";

		}

		for (int i = 0; i < merged.length; i++) {
			boolean control = false;

			for (int j = 0; j < arr1.length; j++) {
				if (merged[i].equals(arr1[j])) {
					suffixArray[i] = sa1[j];
					control = true;
				} else
					continue;
			}

			if (!control) {
				for (int k = 0; k < arr2.length; k++) {

					if (merged[i].equals(arr2[k])) {
						suffixArray[i] = sa2[k];
					} else
						continue;
				}
			}

		}

		return suffixArray;

	}

	public static int min(int start, int end, String[] SA, int[] LCP) {

		int var1 = 0;
		int var2 = 0;
		int minimum = 100;

		int temp = 0;

		String startS = start + "(1)";
		String endS = end + "(2)";

		for (int i = 0; i < SA.length; i++) {
			if (startS.equals(SA[i])) {
				var1 = i + 1;
			} else if (endS.equals(SA[i])) {
				var2 = i;
			} else {
				continue;
			}

		}

		if (var1 > var2) {
			temp = var1;
			var1 = var2;
			var2 = temp;
		}

		for (int i = var1; i < var2; i++) {

			if (LCP[i] < minimum) {
				minimum = LCP[i];
			}

		}

		return minimum;

	}

}