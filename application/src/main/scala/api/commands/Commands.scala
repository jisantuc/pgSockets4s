package com.azavea.pgsockets4s.api.commands

import com.monovore.decline._

object Commands {

  final case class RunServer()

  private def runServerOpts: Opts[RunServer] =
    Opts.subcommand("serve", "Runs web service") {
      Opts { RunServer() }
    }

  def applicationCommand: Command[Product] =
    Command("", "Welcome to the jungle") {
      runServerOpts
    }

}
