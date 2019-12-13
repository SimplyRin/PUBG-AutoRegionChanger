package net.simplyrin.pubgautoregionchanger;

import java.util.Scanner;

/**
 * Created by SimplyRin on 2019/12/14.
 *
 * Copyright (c) 2019 SimplyRin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class Main {

	public static enum Region {
		TOKYO, CHINA
	}

	public static void main(String[] args) {
		new Main().run();
	}

	public void run() {
		System.out.println("PUBG Auto Region Changer v1.0");

		boolean bool = false;

		Thread thread = new Thread(() -> {
			this.changeTimezone(Region.TOKYO);
		});
		Runtime.getRuntime().addShutdownHook(thread);

		this.changeTimezone(Region.TOKYO);

		while (true) {
			boolean isRun = this.isRunTslGame();

			if (bool != isRun) {
				bool = isRun;

				if (bool) {
					this.changeTimezone(Region.CHINA);
				} else {
					this.changeTimezone(Region.TOKYO);
				}
			}

			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
	}

	public void changeTimezone(Region region) {
		System.out.println("Time zone set to '" + region.name() + "'");

		Runtime runtime = Runtime.getRuntime();

		try {
			String[] list = null;

			if (region.equals(Region.TOKYO)) {
				list = new String[] { "tzutil", "/s", "Tokyo " + "Standard " + "Time"};
			}
			else if (region.equals(Region.CHINA)) {
				list = new String[] { "tzutil", "/s", "China " + "Standard " + "Time"};
			}

			/* switch (region) {
			case TOKYO:
				list = new String[] { "tzutil", "/s", "Japan" + " Standard" + " Time"};
			case CHINA:
				list = new String[] { "tzutil", "/s", "China" + " Standard" + " Time"};
			} */

			// System.out.println("Cmd: " + list[2]);

			Process process = runtime.exec(list);

			Scanner scanner = new Scanner(process.getInputStream());
			while (true) {
				if (!scanner.hasNext()) {
					break;
				}

				System.out.println(scanner.nextLine());
			}

			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isRunTslGame() {
		Runtime runtime = Runtime.getRuntime();

		Process process = null;
		try {
			process = runtime.exec("tasklist");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		Scanner scanner = new Scanner(process.getInputStream());
		while (true) {
			if (!scanner.hasNext()) {
				break;
			}

			String[] args = scanner.nextLine().trim().replaceAll("( )+", " ").split(" ");
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("TslGame.exe")) {
					scanner.close();
					return true;
				}
			}
		}

		scanner.close();

		return false;
	}

}
