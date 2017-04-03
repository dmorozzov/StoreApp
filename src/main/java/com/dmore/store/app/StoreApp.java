package com.dmore.store.app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.dmore.store.config.RootSpringConfig;
import com.dmore.store.entity.AbstractStoreItem;
import com.dmore.store.entity.StoreItemFactory;
import com.dmore.store.services.StoreItemService;

public class StoreApp {

	private static final String COMMAND_IMPORT = "import";
	private static final String COMMAND_EXPORT = "export";
	private static final String COMMAND_EXIT = "exit";
	
	private StoreItemService storeItemService;
	
	public StoreApp() {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(RootSpringConfig.class);
		storeItemService = (StoreItemService) applicationContext.getBean("storeItemService");
	}

	public static void main(String[] args) {

		StoreApp storeApp = new StoreApp();
		storeApp.startApp();
	}
	
	private void startApp() {
		String command = "";
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print(">");
			command = scanner.nextLine();
			
			if (COMMAND_EXIT.equalsIgnoreCase(command)) {
				System.out.println("exit.");
				break;
			}
			
			String[] parts = command.split(" ");
			if (parts.length != 2) {
				System.out.println("Illegal command");
				continue;
			}
			if (COMMAND_IMPORT.equalsIgnoreCase(parts[0])) {
				importFile(parts[1]);
			} else if (COMMAND_EXPORT.equalsIgnoreCase(parts[0])) {
				exportFile(parts[1]);
			} else {
				System.out.println("unknown command");
			}
		}
		scanner.close();
	}

	private void importFile(String filePath) {
		Path path = Paths.get(filePath);
		if (!Files.exists(path) || !Files.isRegularFile(path)) {
			System.out.println("file not exists");
			return;
		}
		try (Stream<String> stream = Files.lines(path)) {
			stream.map(x -> StoreItemFactory.createFromString(x)).forEach(x -> storeItemService.saveOrUpdateItem(x));
		} catch (Exception ex) {
			System.out.println("can't parse file: " + ex.getMessage());
		}
	}

	private void exportFile(String filePath) {
		Path path = Paths.get(filePath);
		if (Files.isDirectory(path)) {
			System.out.println("wrong file path");
			return;
		}

		Map<String, Set<AbstractStoreItem>> groupedItems = storeItemService.findAll().stream().collect(Collectors.groupingBy(x -> x.getTypeLabel(), HashMap::new, Collectors.toSet()));
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
			for ( Map.Entry<String, Set<AbstractStoreItem>> entry : groupedItems.entrySet()) {
				bw.write(entry.getKey()+":");
				bw.newLine();
				
				for (AbstractStoreItem item : entry.getValue()) {
					bw.write("   ");
					bw.write(item.toString());
					bw.newLine();
				}
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
