var gulp = require('gulp');

/*
 * Copies the js files and index.html to the folder
 * linked to the xcode project.
*/
gulp.task('default', function() {
    gulp.src('./src/js/*').
        pipe(gulp.dest('./build/www'));
    gulp.src('./example/web/*').
        pipe(gulp.dest('./build/www'));
});
