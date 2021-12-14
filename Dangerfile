# Sometimes it's a README fix, or something like that - which isn't relevant for
# including in a project's CHANGELOG for example
declared_trivial = github.pr_title.include? "#trivial"

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR
warn("Big PR") if git.lines_of_code > 500

# Don't let testing shortcuts get into master by accident
fail("fdescribe left in tests") if `grep -r fdescribe specs/ `.length > 1
fail("fit left in tests") if `grep -r fit specs/ `.length > 1

## Metadata checks
markdown "> Please provide a summary in the Pull Request description to help your colleagues to understand the MR purpose." if github.pr_body.length < 5

has_assignee = github.pr_json["assignee"] != nil
markdown "> This pull request does not have any assignee yet. Setting an assignee clarifies who needs to take action on the pull request at any given time." unless has_assignee

## Run detekt
# Run detection on files that added or changed only and comment automatically
kotlin_files = (git.added_files + git.modified_files).select{ |file| file.end_with?(".kt", ".kts") }

unless kotlin_files.empty?
  puts `./gradlew detekt`
  checkstyle_format.base_path = Dir.pwd
  checkstyle_format.report "build/reports/detekt/detekt.xml"
else
  puts 'No changed kotlin files in this MR so Detekt will not run'
end

## Android lint
#  puts 'Begin task runChecksForDanger'
#  puts `./gradlew runChecksForDanger`
#
#  lint_report_path = "app/build/reports/lint-results.xml"
#  if File.file?(lint_report_path)
#  android_lint.skip_gradle_task = true
#  android_lint.report_file = lint_report_path
#  #android_lint.severity = "Error"
#  android_lint.filtering = true
#android_lint.lint(inline_mode: true)
#  android_lint.lint
#  else
#  put 'Android lint has found no report'
#  end
