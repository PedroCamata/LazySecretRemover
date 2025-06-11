package lazySecretRemover;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LazySecretRemoverService {
	private Runtime rt;
	private String repoFolder;
	
	public LazySecretRemoverService() {
		rt = Runtime.getRuntime();
		repoFolder = "";
	}

	public void cloneRepo(String gitRepoUrl) {
		// Set name of the repo folder from last '/' in the gitRepoUrl
		String repoName = gitRepoUrl.substring(gitRepoUrl.lastIndexOf('/') + 1);
		if (repoName.endsWith(".git")) {
			repoName = repoName.substring(0, repoName.length() - 4);
		}
		repoFolder = repoName;
		File original = new File(repoFolder);
		
		if (!original.exists()) {
			runCommands("git", "clone", gitRepoUrl);
			fetchAllBranches(repoFolder);
			System.out.println("Repository cloned successfully");
		} else {
			System.out.println("Repository has already been cloned");
		}

		// Backup git repo in a different folder			
		File backup = new File(repoFolder + "_backup");
		if (!backup.exists()) {
			try {
				copyFolder(original, backup);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			System.out.println("Backup created successfully");
		} else {
			System.out.println("Backup has already been created");
		}
	}

	public void removeSecrets(String text) {
		System.out.print("Removing text:\n" + text);

		// Get the secrets address from the lines of text
		String[] secrets = text.split("\n");
		File secretsFile = createTempFile(secrets);
		
		runCommands("java", "-jar", "bfg.jar", "--replace-text", secretsFile.getAbsolutePath(), repoFolder);
		
		deleteTempFile(secretsFile);
		System.out.println("Secrets removed successfully.");
	}

	public void removeFiles(String text) {
		System.out.print("Removing files:\n" + text);

		// Get the file address from the lines of text
		String[] files = text.split("\n");
		
		for (String file : files) {
			runCommands("java", "-jar", "bfg.jar", "--delete-files", file, "--no-blob-protection", repoFolder);
		}
		
		System.out.println("Files removed successfully.");
	}

	// TODO: To be implemented future implementation
	public void gitPushForce() {
		runCommands("git", "--git-dir=" + repoFolder, "push", "--force");
		System.out.println("Force push completed successfully.");
	}
	
	private void runCommands(String... command) {
		try {
			ProcessBuilder builder = new ProcessBuilder(command);
			builder.inheritIO();
			Process pr = builder.start();
			pr.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void fetchAllBranches(String repoFolder) {
		try {
			ProcessBuilder builder = new ProcessBuilder(
				"bash", "-c",
				"git fetch --all && " +
				"for remote_branch in $(git branch -r | grep -v '\\->'); do " +
				"git branch --track \"${remote_branch#*/}\" \"$remote_branch\" 2>/dev/null; done");
			builder.directory(new File(repoFolder));
			builder.inheritIO();
			Process pr = builder.start();
			pr.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	private void copyFolder(File source, File destination) throws IOException {
		if (source.isDirectory()) {
			if (!destination.exists()) {
				destination.mkdir();
			}
			String[] files = source.list();
			if (files != null) {
				for (String file : files) {
					copyFolder(new File(source, file), new File(destination, file));
				}
			}
		} else {
			java.nio.file.Files.copy(source.toPath(), destination.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	private File createTempFile(String[] secrets) {
        // Create a temporary .txt file with the secrets
        File secretsFile = new File("secrets-temp.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(secretsFile))) {
            for (String secret : secrets) {
                if (!secret.trim().isEmpty()) {
                    writer.write(secret);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to write secrets to temp file.");
            e.printStackTrace();
        }
        
        return secretsFile;
	}
	
	private void deleteTempFile(File secretsFile) {
		// Delete the temporary secrets file
        if (secretsFile.exists()) {
            if (!secretsFile.delete()) {
                System.err.println("Warning: Could not delete temporary secrets file.");
            }
        }
	}
}
