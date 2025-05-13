{ pkgs ? import <nixpkgs> {} }:
  pkgs.mkShell {
    # nativeBuildInputs is usually what you want -- tools you need to run
    nativeBuildInputs = 
    with pkgs.buildPackages; [
    	eclipses.eclipse-java
    	pkgs.jdk23
   	];
}
