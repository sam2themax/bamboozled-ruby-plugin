package com.alienfast.bamboozled.ruby.tasks.rake;

import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import com.alienfast.bamboozled.ruby.rt.RubyCapabilityDefaultsHelper;
import com.alienfast.bamboozled.ruby.rt.RubyLocator;
import com.alienfast.bamboozled.ruby.rt.RubyRuntime;
import com.alienfast.bamboozled.ruby.tasks.AbstractBundleExecCommandBuilder;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.google.common.base.Preconditions;

/**
 * Builder to assemble the rake command list.
 * <p/>
 * TODO Need to reconsider the design of this class, probably moving to properties over a list, with the command list being built in the build method.
 */
public class RakeCommandBuilder extends AbstractBundleExecCommandBuilder<RakeCommandBuilder> {

    public static final String XVFB_RUN_ARG = "-a";
    public static final String RAKE_COMMAND = "rake";

    public static final String RAKEFILE_ARG = "-f";
    public static final String RAKELIBDIR_ARG = "--rakelibdir";

    public static final String TRACE_ARG = "--trace";

    public RakeCommandBuilder(CapabilityContext capabilityContext, RubyLocator rvmRubyLocator, RubyRuntime rubyRuntime,
            String rubyExecutablePath) {

        super( capabilityContext, rvmRubyLocator, rubyRuntime, rubyExecutablePath );
    }

    protected String getXvfbRunExecutablePath() {

        final Capability capability = getCapabilityContext().getCapabilitySet().getCapability(
                RubyCapabilityDefaultsHelper.XVFB_RUN_CAPABILITY );
        Preconditions.checkNotNull( capability, "Capability for xvfb-run.  Please be sure to \"Detect server capabilities\" in the Administration console, and the the xvfb-run path is valid." );
        final String exe = capability.getValue();
        Preconditions.checkNotNull( exe, "xvfbRunExecutable" );
        return exe;
    }

    /**
     * Will conditionally prepend the xvfb-run command if xvfb-run flag is "true".
     *
     * @param xvfbRun String which takes null or "true".
     * @return T command builder.
     */
    public RakeCommandBuilder addIfXvfbRun( @Nullable String xvfbRunFlag ) {

        if ( BooleanUtils.toBoolean( xvfbRunFlag ) ) {
            this.log.info( "Adding {} {}", getXvfbRunExecutablePath(), XVFB_RUN_ARG );

            getCommandList().add( getXvfbRunExecutablePath() );
            getCommandList().add( XVFB_RUN_ARG );
            this.log.info( "Added {} {}: {}", getXvfbRunExecutablePath(), XVFB_RUN_ARG, getCommandList().toString() );
        }
        return this;
    }

    /**
     * Append the rake executable to the command list.
     *
     * @param bundleFlag String which takes null or "true", this indicates whether to use short command or full path.
     * @return Rake command builder.
     */
    public RakeCommandBuilder addRakeExecutable( @Nullable String bundleFlag ) {

        if ( BooleanUtils.toBoolean( bundleFlag ) ) {
            getCommandList().add( RAKE_COMMAND );
        }
        else {
            getCommandList().add(
                    getRubyLocator().buildExecutablePath( getRubyRuntime().getRubyRuntimeName(), getRubyExecutablePath(), RAKE_COMMAND ) );
        }
        return this;
    }

    /**
     * Will conditionally append rake file parameter if rake file is not empty.
     *
     * @param rakeFile String which takes either null or a file path.
     * @return Rake command builder.
     */
    public RakeCommandBuilder addIfRakeFile( @Nullable String rakeFile ) {

        if ( StringUtils.isNotEmpty( rakeFile ) ) {
            getCommandList().add( RAKEFILE_ARG );
            getCommandList().add( rakeFile );
        }
        return this;
    }

    /**
     * Will conditionally append rake lib directory parameter if rake file is not empty.
     *
     * @param rakeLibDir String which takes either null or a directory path.
     * @return Rake command builder.
     */
    public RakeCommandBuilder addIfRakeLibDir( @Nullable String rakeLibDir ) {

        if ( StringUtils.isNotEmpty( rakeLibDir ) ) {
            getCommandList().add( RAKELIBDIR_ARG );
            getCommandList().add( rakeLibDir );
        }
        return this;
    }

    /**
     * Will conditionally append the trace switch if trace flag is "true"..
     *
     * @param traceFlag String which takes null or "true".
     * @return Rake command builder.
     */
    public RakeCommandBuilder addIfTrace( @Nullable String traceFlag ) {

        if ( BooleanUtils.toBoolean( traceFlag ) ) {
            getCommandList().add( TRACE_ARG );
        }
        return this;
    }

    /**
     * Will append the supplied list of targets to the command list.
     *
     * @param targets List of targets.
     * @return Rake command builder.
     */
    public RakeCommandBuilder addTargets( List<String> targets ) {

        getCommandList().addAll( targets );
        return this;
    }
}
