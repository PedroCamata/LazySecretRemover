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
		System.out.print("\nCloneRepo method");

		try {
			ProcessBuilder builder = new ProcessBuilder("git", "clone", gitRepoUrl); // "--mirror", 
			builder.inheritIO();
			Process pr = builder.start();
			pr.waitFor();

			// Set name of the repo folder from last '/' in the gitRepoUrl
			String repoName = gitRepoUrl.substring(gitRepoUrl.lastIndexOf('/') + 1);
			if (repoName.endsWith(".git")) {
				repoName = repoName.substring(0, repoName.length() - 4);
			}
			repoFolder = repoName;

			// Backup git repo in a different folder
			File original = new File(repoFolder);
			File backup = new File(repoFolder + "_backup");
			if (original.exists()) {
				copyFolder(original, backup);
			}

			// Send pop message when has finished
			System.out.println("Repository cloned and backup created successfully.");

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void removeSecrets(String text) {
		System.out.print(text);

		// Get the secrets address from the lines of text
		String[] secrets = text.split("\n");
		
		File secretsFile = createTempFile(secrets);
		
		try {
			
			ProcessBuilder builder = new ProcessBuilder("java", "-jar", "bfg.jar", "--replace-text", secretsFile.getAbsolutePath(), repoFolder);
			builder.inheritIO();
			Process pr = builder.start();
			pr.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		deleteTempFile(secretsFile);
		System.out.println("Secrets removed successfully.");
	}

	public void removeFiles(String text) {
		System.out.print(text);

		// Get the file address from the lines of text
		String[] files = text.split("\n");

		for (String file : files) {
			try {
				ProcessBuilder builder = new ProcessBuilder("java", "-jar", "bfg.jar", "--delete-files", file, "--no-blob-protection", repoFolder);
				builder.inheritIO();
				Process pr = builder.start();
				pr.waitFor();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Files removed successfully.");
	}

	public void gitPushForce() {
		try {
			ProcessBuilder builder = new ProcessBuilder("git", "--git-dir=" + repoFolder, "push", "--force");
			builder.inheritIO();
			Process pr = builder.start();
			pr.waitFor();
			System.out.println("Force push completed successfully.");
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
