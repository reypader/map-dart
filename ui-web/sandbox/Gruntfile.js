/*global module:false*/
module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    // Metadata.
    pkg : grunt.file.readJSON('package.json'),

    // Task configuration.
    jshint : {
      options : {
        curly : true,
        eqeqeq : true,
        immed : true,
        latedef : true,
        newcap : true,
        noarg : true,
        sub : true,
        undef : true,
        unused : true,
        boss : true,
        eqnull : true,
        globals : {}
      },
      gruntfile : {
        src : 'Gruntfile.js'
      },
      lib_test : {
        src : [ 'lib/**/*.js', 'test/**/*.js' ]
      }
    },
    concat : {
      options : {
        stripBanners : true
      },
      dist : {
        src : [ 'js/**/*.js' ],
        dest : 'dist/<%= pkg.name %>.js'
      }
    },
    uglify : {
      dist : {
        src : '<%= concat.dist.dest %>',
        dest : 'dist/<%= pkg.name %>.min.js'
      }
    },
    watch : {
      gruntfile : {
        files : '<%= jshint.gruntfile.src %>',
        tasks : [ 'jshint:gruntfile' ]
      },
      lib_test : {
        files : '<%= jshint.lib_test.src %>',
        tasks : [ 'jshint:lib_test', 'jasmine' ]
      }
    }
  });

  // These plugins provide necessary tasks.
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-nodeunit');
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-watch');

  // Default task.
  grunt.registerTask('default', [ 'jshint', 'concat', 'uglify' ]);

};
