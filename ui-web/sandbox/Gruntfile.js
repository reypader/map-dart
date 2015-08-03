/*global module:false*/
module.exports = function (grunt) {
  var globalConfig = {
    images: 'app/images',
    css: 'app/styles',
    fonts: 'app/fonts',
    scripts: 'app/modules',
    bower_path: 'bower_components'
  };
  // Project configuration.
  grunt.initConfig({
      globalConfig: globalConfig,
      clean: ['dist', '<%= globalConfig.fonts %>/*.*', 'app/require.js', '<%= globalConfig.css %>/compiled-bootstrap','<%= globalConfig.css %>/*.*'],
      jshint: {
        options: {
          curly: true,
          eqeqeq: true,
          immed: true,
          latedef: true,
          newcap: true,
          noarg: true,
          sub: true,
          undef: true,
          unused: true,
          boss: true,
          eqnull: true,
          globals: {}
        },
        all: {
          src: [
            'Gruntfile.js',
            '<%= globalConfig.scripts %>/**/*.js'
          ]
        },
        test: {
          src: ['test/spec/**/*.js']
        }
      },
      customize_bootstrap: {
        task: {
          options: {
            bootstrapPath: '<%= globalConfig.bower_path %>/bootstrap',
            src: '<%= globalConfig.css %>/customized-bootstrap-less',
            dest: '<%= globalConfig.css %>/compiled-bootstrap',
          }
        }
      },
      less: {
        task: {
          src: '<%= customize_bootstrap.task.options.dest %>/bootstrap.less',
          dest: '<%= customize_bootstrap.task.options.dest %>/bootstrap.css'
        }
      },
      requirejs: {
        compile: {
          options: {
            baseUrl: '<%= globalConfig.scripts %>',
            mainConfigFile: '<%= globalConfig.scripts %>/main.js',
            name: 'main',
            out: 'dist/modules/main.js',
            preserveLicenseComments: false
          }
        }
      }
      ,
      bowerRequirejs: {
        target: {
          rjsConfig: '<%= globalConfig.scripts %>/main.js',
          options: {
            transitive: true
          }
        }
      }
      ,
      copy: {
        release: {
          files: [
            {
              expand: true,
              cwd: 'app',
              src: ['require.js'],
              dest: 'dist/',
              filter: 'isFile'
            },
            {
              expand: true,
              cwd: 'app',
              src: ['**/*.html'],
              dest: 'dist/',
              filter: 'isFile'
            },
            {
              expand: true,
              cwd: 'app',
              src: ['fonts/**', 'images/**'],
              dest: 'dist/',
              filter: 'isFile'
            },
            {
              expand: true,
              cwd: '<%= globalConfig.css %>',
              src: ['**/*.min.css'],
              dest: 'dist/styles',
              filter: 'isFile'
            },
            {
              expand: true,
              flatten: true,
              cwd: 'app',
              src: ['require.js'],
              dest: 'dist/modules/',
              filter: 'isFile'
            }
          ]
        }
        ,
        init: {
          files: [
            {
              expand: true,
              flatten: true,
              src: ['node_modules/requirejs/require.js'],
              dest: 'app/',
              filter: 'isFile'
            },
            //{
            //  expand: true,
            //  flatten: true,
            //  src: ['bower_components/bootstrap/dist/css/bootstrap-theme.min.css'],
            //  dest: '<%= globalConfig.css %>/raw',
            //  filter: 'isFile'
            //},
            {
              expand: true,
              flatten: true,
              src: ['<%= globalConfig.bower_path %>/bootstrap/dist/fonts/*'],
              dest: '<%= globalConfig.fonts %>/',
              filter: 'isFile'
            },
            {
              expand: true,
              flatten: true,
              src: ['<%= globalConfig.bower_path %>/font-awesome/fonts/*'],
              dest: '<%= globalConfig.fonts %>/',
              filter: 'isFile'
            }
          ]
        }
      }
      ,
      connect: {
        options: {
          port: 9000,
          livereload: 35729,
          hostname: '*' // * = accessible from anywhere ; default: localhost
        }
        ,
        livereload: {
          options: {
            open: true,
            base: [''] // '.tmp',
          }
        }
      }
      ,
      cssmin: {
        development: {
          expand: true,
          cwd: '<%= globalConfig.css %>/',
          src: ['*.css', '!*.min.css', '!raw/*'],
          dest: '<%= globalConfig.css %>/',
          ext: '.min.css'
        }
      },
      watch: {
        options: {
          livereload: true
        },
        styles: {
          files: ['<%= globalConfig.css %>/{,*/}*.{scss,sass}'],
          tasks: ['cssmin']
        },
        livereload: {
          options: {
            livereload: '<%= connect.options.livereload %>'
          },
          files: ['*.html']
        }
      },
      //For CSS only. JS files are handled by RequireJS.
      concat: {
        options: {
          separator: '\n',
        },
        signin: {
          src: [
            '<%= less.task.dest %>',
            '<%= globalConfig.bower_path %>/font-awesome/css/font-awesome.min.css',
            '<%= globalConfig.bower_path %>/bootstrap-social/bootstrap-social.css',
            '<%= globalConfig.css %>/raw/signin.css'
          ],
          dest: '<%= globalConfig.css %>/signin.css',
        },
        welcome: {
          src: [
            '<%= less.task.dest %>',
            '<%= globalConfig.css %>/raw/main.css'
          ],
          dest: '<%= globalConfig.css %>/welcome.css',
        },
        app: {
          src: [
            '<%= less.task.dest %>',
            '<%= globalConfig.css %>/raw/main.css'
          ],
          dest: '<%= globalConfig.css %>/app.css',
        }
      },
      karma: {
        unit: {
          configFile: 'karma.conf.js',
          singleRun: true
        }
      }
    }
  )
  ;

  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-requirejs');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-connect');
  grunt.loadNpmTasks('grunt-contrib-cssmin');
  grunt.loadNpmTasks('grunt-bower-requirejs');
  grunt.loadNpmTasks('grunt-customize-bootstrap');
  grunt.loadNpmTasks('grunt-karma');


  grunt.registerTask('test', ['karma']);
  grunt.registerTask('release', ['test', 'init', 'requirejs', 'copy:release']);
  grunt.registerTask('compile-style', ['customize_bootstrap', 'less', 'concat', 'cssmin']);
  grunt.registerTask('init', ['clean', 'copy:init', 'bowerRequirejs', 'compile-style']);

};
