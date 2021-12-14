message("Thanks for your pull request!")

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR
warn("Big PR") if git.lines_of_code > 500

fail("Please provide a summary in the Pull Request description to help your colleagues to understand the MR purpose.") if github.pr_body.length < 5

has_assignee = github.pr_json["assignee"] != nil
markdown "> This pull request does not have any assignee yet. Setting an assignee clarifies who needs to take action on the pull request at any given time." unless has_assignee

# write the comments on the files that are on the scope of the pull request (modified files)
github.dismiss_out_of_range_messages

# Collect reports from Detekt
detekt_report_path = "build/reports/code_quality_tools/detekt-results.xml"
if File.file?(detekt_report_path)
    checkstyle_format.base_path = Dir.pwd
    checkstyle_format.report detekt_report_path
else
    puts 'no Detekt reports found'
end

# Collect reports from Lint
# lint_report_path = "build/reports/code_quality_tools/lint-results.xml"
# if File.file?(lint_report_path)
#     android_lint.skip_gradle_task = true
#     android_lint.report_file = lint_report_path
#     #android_lint.severity = "Error"
#     android_lint.filtering = true
#     android_lint.lint(inline_mode: true)
#     android_lint.lint
# else
#     puts 'no Android Lint reports found'
# end
