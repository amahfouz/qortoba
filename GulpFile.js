var gulp = require('gulp');

javac = require('gulp-javac');

/*
 * Copies the js files and index.html to the folder
 * linked to the xcode project.
*/
gulp.task('web-copy', function() {
    gulp.src('./src/js/*').
        pipe(gulp.dest('./build/www'));
    gulp.src('./example/web/*').
        pipe(gulp.dest('./build/www'));
});

gulp.task('javac-tools', function() {
   gulp.src('./src/android/com/mahfouz/qortoba/tools/*.java').
      pipe(javac.javac().addLibraries('')).
      pipe(javac.jar('qortoba-tools.jar')).
      pipe(gulp.dest('/Users/amahfouz/code/qortoba/build/java'));
});
