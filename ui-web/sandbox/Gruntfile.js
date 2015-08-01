/*global module:false*/
module.exports = function (grunt) {

    // Project configuration.
    grunt.initConfig({
        // Metadata.
        pkg: grunt.file.readJSON('package.json'),

        // Task configuration.
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
            gruntfile: {
                src: 'Gruntfile.js'
            },
            lib_test: {
                src: ['lib/**/*.js', 'test/**/*.js']
            }
        },
        concat: {
            options: {
                stripBanners: true
            },
            dist: {
                src: ['js/**/*.js'],
                dest: 'dist/<%= pkg.name %>.js'
            }
        },
        uglify: {
            dist: {
                src: '<%= concat.dist.dest %>',
                dest: 'dist/<%= pkg.name %>.min.js'
            }
        },
        watch: {
            gruntfile: {
                files: '<%= jshint.gruntfile.src %>',
                tasks: ['jshint:gruntfile']
            },
            lib_test: {
                files: '<%= jshint.lib_test.src %>',
                tasks: ['jshint:lib_test', 'jasmine']
            }
        },
        customize_bootstrap: {
            task: {
                options: {
                    bootstrapPath: 'bower_components/bootstrap',
                    src: 'app/styles/customized-bootstrap-less',
                    dest: 'app/styles/',
                }
            }
        },
        less: {
            task: {
                src: '<%= customize_bootstrap.task.options.dest %>/bootstrap.less',
                dest: 'app/styles/bootstrap.css'
            }
        },
        clean: ['dist'],
        requirejs: {
            compile: {
                options: {
                    baseUrl: 'app/modules',
                    mainConfigFile: 'app/modules/main.js',
                    name: 'main',
                    out: 'dist/modules/main.js'
                }
            }
        },
        bowerRequirejs: {
            target: {
                rjsConfig: 'app/modules/main.js',
                options: {
                    transitive: true
                }
            }
        },
        copy: {
            release: {
                files: [
                    // includes files within path
                    {
                        expand: true,
                        cwd: 'app',
                        src: ['**/*.html'],
                        dest: 'dist/',
                        filter: 'isFile'
                    },
                    {
                        expand: true,
                        cwd: 'app/styles',
                        src: ['**/*.css'],
                        dest: 'dist/styles',
                        filter: 'isFile'
                    },
                    {
                        expand: true,
                        flatten: true,
                        cwd: 'app/modules',
                        src: ['require.js'],
                        dest: 'dist/modules/',
                        filter: 'isFile'
                    }
                ]
            },
            init: {
                files: [
                    {
                        expand: true,
                        flatten: true,
                        src: ['node_modules/**/require.js'],
                        dest: 'app/modules/',
                        filter: 'isFile'
                    },
                    {
                        expand: true,
                        flatten: true,
                        src: ['bower_components/**/bootstrap-theme.min.css'],
                        dest: 'app/styles/',
                        filter: 'isFile'
                    }
                ]
            }
        },
        karma: {
            unit: {
                configFile: 'karma.conf.js'
            }
        }
    });

    // These plugins provide necessary tasks.
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-requirejs');
    grunt.loadNpmTasks('grunt-bower-requirejs');
    grunt.loadNpmTasks('grunt-customize-bootstrap');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-karma');


    grunt.registerTask('test', ['karma']);
    grunt.registerTask('release', ['build', 'clean', 'requirejs', 'copy:release']);
    grunt.registerTask('compile-style', ['customize_bootstrap', 'less']);
    grunt.registerTask('build', ['copy:init', 'bowerRequirejs', 'compile-style']);

};
